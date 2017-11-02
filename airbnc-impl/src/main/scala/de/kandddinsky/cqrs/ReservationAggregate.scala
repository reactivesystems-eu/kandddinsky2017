package de.kandddinsky.cqrs

import java.time.LocalDate

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity

/**
  */
class ReservationAggregate extends PersistentEntity {
  override type Command = ReservationCommand[_]
  override type Event = ReservationEvent
  override type State = ReservationState

  override def initialState =
    ReservationState(Map[LocalDate, List[ReservationData]]()
                       .withDefaultValue(List[ReservationData]()),
                     Map.empty[LocalDate, ReservationData])

  override def behavior: Behavior = { _ =>
    Actions()
      .onCommand[RequestReservation, Done] {
        case (RequestReservation(reservationData), ctx, state) =>
          ctx.thenPersist(
            ReservationRequested(reservationData)
          ) { _ =>
            ctx.reply(Done)
          }
      }
      .onEvent {
        case (ReservationRequested(reservationData), state) =>
          val reservations = reservationData +: state.reservationRequests(
            reservationData.startingDate)
          state.copy(
            reservationRequests = state.reservationRequests
              + (reservationData.startingDate -> reservations))
      }
  }
}
