package de.kandddinsky.cqrs

import java.time.LocalDate

import play.api.libs.json.{Format, Json}

/**
  * hello-lagom
  */
case class ReservationData(accomodation: String,
                           guest: String,
                           host: String,
                           startingDate: LocalDate,
                           duration: Int)

object ReservationData {
  implicit val format: Format[ReservationData] = Json.format
}
