package de.kandddinsky.cqrs

import java.util.Date

/**
  * hello-lagom
  */
case class ReservationData(accomodation: String,
                           guest: String,
                           host: String,
                           startingDate: Date,
                           duration: Int)
