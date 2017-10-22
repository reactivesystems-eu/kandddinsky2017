package de.kandddinsky.cqrs

import play.api.libs.json._

/**
  * hello-lagom
  */
case class ReservationState(
    reservationRequests: Map[String, List[ReservationData]],
    confirmReservations: Map[String, ReservationData])

object ReservationState {

  implicit val format: Format[ReservationState] = Json.format
}
