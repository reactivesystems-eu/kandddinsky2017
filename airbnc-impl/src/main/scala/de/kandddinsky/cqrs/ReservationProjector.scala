package de.kandddinsky.cqrs

import akka.Done
import com.datastax.driver.core.{BoundStatement, PreparedStatement}
import com.lightbend.lagom.scaladsl.persistence.{
  EventStreamElement,
  ReadSideProcessor
}
import com.lightbend.lagom.scaladsl.persistence.cassandra.{
  CassandraReadSide,
  CassandraSession
}

import scala.concurrent.{ExecutionContext, Future, Promise}

class ReservationProjector(
    session: CassandraSession,
    readSide: CassandraReadSide)(implicit ec: ExecutionContext)
    extends ReadSideProcessor[ReservationEvent] {

  override def buildHandler() =
    readSide
      .builder[ReservationEvent]("hostreservations")
      .setGlobalPrepare(createTable)
      .setPrepare(tag => prepareWrite())
      .setEventHandler[ReservationRequested](process)
      .build()

  private def process(eventElement: EventStreamElement[ReservationRequested])
    : Future[List[BoundStatement]] = {
    writeHostReservation.map { ps =>
      val bindWriteTitle = ps.bind()
      bindWriteTitle.setString("host", eventElement.event.reservationData.host)
      bindWriteTitle.setString("accomodation",
                               eventElement.event.reservationData.accomodation)
      bindWriteTitle.setString("guest",
                               eventElement.event.reservationData.guest)
      bindWriteTitle.setString(
        "startingDate",
        eventElement.event.reservationData.startingDate.toString)
      bindWriteTitle.setInt("duration",
                            eventElement.event.reservationData.duration)
      List(bindWriteTitle)
    }
  }

  override def aggregateTags = Set(ReservationEvent.Tag)

  private def createTable(): Future[Done] =
    session.executeCreateTable(
      "CREATE TABLE IF NOT EXISTS hostreservations ( " +
        "host TEXT, accomodation TEXT, guest TEXT, startingDate TEXT, duration INT, PRIMARY KEY (host, accomodation, startingDate, guest))")

  private val writePromise = Promise[PreparedStatement] // initialized in prepare
  private def writeHostReservation: Future[PreparedStatement] =
    writePromise.future

  private def prepareWrite(): Future[Done] = {
    val f = session.prepare(
      "INSERT INTO hostreservations (host, accomodation, guest, startingDate, duration) VALUES (?, ?, ?, ?, ?)")
    writePromise.completeWith(f)
    f.map(_ => Done)
  }

}
