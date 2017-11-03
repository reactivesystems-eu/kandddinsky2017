package de.kandddinsky.cqrs

import play.api.libs.json.{Format, Json, __}
import julienrf.json.derived

sealed trait ReservationNotification

case class ReservationRequestedNotification(reservationData: Reservation)
    extends ReservationNotification

object ReservationRequestedNotification {
  implicit val format: Format[ReservationRequestedNotification] = Json.format
}

case class ReservationRejectedNotification(reservationData: Reservation)
    extends ReservationNotification

object ReservationRejectedNotification {
  implicit val format: Format[ReservationRejectedNotification] = Json.format
}

case class ReservationConfirmedNotification(reservationData: Reservation)
    extends ReservationNotification

object ReservationConfirmedNotification {
  implicit val format: Format[ReservationConfirmedNotification] = Json.format
}

case class ReservationCancelledNotification(reservationData: Reservation)
    extends ReservationNotification

object ReservationCancelledNotification {
  implicit val format: Format[ReservationCancelledNotification] = Json.format
}

object ReservationNotification {
  implicit val format: Format[ReservationNotification] =
    derived.flat.oformat((__ \ "type").format[String])
}
