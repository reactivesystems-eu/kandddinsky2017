package de.kandddinsky.cqrs

import java.time.LocalDate

import com.datastax.driver.core.Row
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession

import scala.concurrent.{ExecutionContext, Future}

class ReservationRepository(session: CassandraSession)(
    implicit ec: ExecutionContext) {

  def getReservationsForHost(host: String): Future[Seq[Reservation]] = {
    session
      .selectAll("SELECT * FROM hostreservations WHERE host = ?", host)
      .map(_.map(convertRowToReservation))
  }

  private def convertRowToReservation(row: Row): Reservation =
    Reservation(row.getString("accomodation"),
                row.getString("guest"),
                row.getString("host"),
                LocalDate.parse(row.getString("startingDate")),
                row.getInt("duration"))

}
