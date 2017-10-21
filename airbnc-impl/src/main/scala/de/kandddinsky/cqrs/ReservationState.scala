package de.kandddinsky.cqrs

import java.time.LocalDate

import play.api.libs.functional.syntax._
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
