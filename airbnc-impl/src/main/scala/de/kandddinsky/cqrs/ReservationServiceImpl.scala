package de.kandddinsky.cqrs

import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.broker.TopicProducer
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry

/**
  * Implementation of the ReservationService.
  */
class ReservationServiceImpl(persistentEntityRegistry: PersistentEntityRegistry,
                             reservationRepository: ReservationRepository)
    extends ReservationService {

  override def requestReservation(accomodation: String) = ServiceCall {
    reservation =>
      val aggregate =
        persistentEntityRegistry.refFor[ReservationAggregate](accomodation)

      val reservationData = ReservationData(
        accomodation = reservation.accomodation,
        guest = reservation.guest,
        host = reservation.host,
        startingDate = reservation.startingDate,
        duration = reservation.duration
      )
      // Ask the entity the Hello command.
      aggregate.ask(RequestReservation(reservationData))
  }

  /**
    * The reservation notification events topic.
    */
  override def reservationNotifications(): Topic[ReservationNotification] =
    TopicProducer.singleStreamWithOffset { fromOffset =>
      persistentEntityRegistry
        .eventStream(ReservationEvent.Tag, fromOffset)
        .map(ev => (convertEvent(ev.event), ev.offset))
    }

  def convertEvent(event: ReservationEvent): ReservationNotification =
    event match {
      case ReservationRequested(reservation) =>
        ReservationRequestedNotification(reservation)
      case ReservationConfirmed(reservation) =>
        ReservationConfirmedNotification(reservation)
      case ReservationRejected(reservation) =>
        ReservationRejectedNotification(reservation)
      case ReservationCancelled(reservation) =>
        ReservationCancelledNotification(reservation)
    }
}
