package de.kandddinsky.cqrs

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import play.api.libs.json.{Format, Json}

/**
  * hello-lagom
  */
sealed trait ReservationCommand[R] extends ReplyType[R]

case class RequestReservation(reservationData: String)
    extends ReservationCommand[Done]

object RequestReservation {
  implicit val format: Format[RequestReservation] = Json.format
}

case class ConfirmReservation(reservationData: String)
    extends ReservationCommand[Done]

object ConfirmReservation {
  implicit val format: Format[ConfirmReservation] = Json.format
}

case class CancelReservation(reservationData: String)
    extends ReservationCommand[Done]

object CancelReservation {
  implicit val format: Format[CancelReservation] = Json.format
}
