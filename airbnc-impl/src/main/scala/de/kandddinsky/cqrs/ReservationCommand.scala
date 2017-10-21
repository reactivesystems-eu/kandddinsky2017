package de.kandddinsky.cqrs

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import play.api.libs.json.{Format, Json}

/**
  * hello-lagom
  */
sealed trait ReservationCommand[R] extends ReplyType[R]

case class RequestReservation(reservationData: ReservationData)
    extends ReservationCommand[Done]

object RequestReservation {
  implicit val format: Format[RequestReservation] = Json.format
}

case class ConfirmReservation(reservationData: ReservationData)
    extends ReservationCommand[Done]

object ConfirmReservation {
  implicit val format: Format[ConfirmReservation] = Json.format
}

case class CancelReservation(reservationData: ReservationData)
    extends ReservationCommand[Done]

object CancelReservation {
  implicit val format: Format[CancelReservation] = Json.format
}

case class RejectReservation(reservationData: ReservationData)
    extends ReservationCommand[Done]

object RejectReservation {
  implicit val format: Format[CancelReservation] = Json.format
}
