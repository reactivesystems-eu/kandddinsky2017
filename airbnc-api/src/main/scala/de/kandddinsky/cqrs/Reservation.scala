package de.kandddinsky.cqrs

import java.time.LocalDate

import play.api.libs.json.{Format, Json}

case class Reservation(accomodation: String,
                       guest: String,
                       host: String,
                       startingDate: LocalDate,
                       duration: Int)

object Reservation {
  implicit val format: Format[Reservation] = Json.format
}
