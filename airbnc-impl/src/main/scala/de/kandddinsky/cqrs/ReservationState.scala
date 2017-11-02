package de.kandddinsky.cqrs

import java.time.LocalDate

import play.api.libs.json.Json.JsValueWrapper
import play.api.libs.json._

/**
  */
case class ReservationState(
    reservationRequests: Map[LocalDate, List[ReservationData]],
    confirmReservations: Map[LocalDate, ReservationData])

object ReservationState {

  import MapFormats._

  implicit val format: Format[ReservationState] = Json.format

}
