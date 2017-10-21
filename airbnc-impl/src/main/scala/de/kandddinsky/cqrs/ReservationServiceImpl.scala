package de.kandddinsky.cqrs

import java.time.LocalDate

import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.broker.TopicProducer
import com.lightbend.lagom.scaladsl.persistence.{
  EventStreamElement,
  PersistentEntityRegistry
}

/**
  * Implementation of the HellolagomService.
  */
class ReservationServiceImpl(persistentEntityRegistry: PersistentEntityRegistry)
    extends ReservationService {

  override def requestReservation(accomodation: String) = ServiceCall {
    reservation =>
      // Look up the hello-lagom entity for the given ID.

      val ref =
        persistentEntityRegistry.refFor[ReservationAggregate](accomodation)

      val reservationData = ReservationData(
        accomodation = reservation.accomodation,
        guest = reservation.guest,
        host = reservation.host,
        startingDate = reservation.startingDate,
        duration = reservation.duration
      )
      // Ask the entity the Hello command.
      ref.ask(RequestReservation(reservationData))
  }

}
