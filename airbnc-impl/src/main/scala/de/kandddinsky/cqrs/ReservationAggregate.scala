package de.kandddinsky.cqrs

import java.time.LocalDate

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity

/**
  * hello-lagom
  */
class ReservationAggregate extends PersistentEntity {
  override type Command = ReservationCommand[_]
  override type Event = ReservationEvent
  override type State = ReservationState

  override def initialState =
    ReservationState(Map[String, List[ReservationData]]()
                       .withDefaultValue(List[ReservationData]()),
                     Map.empty[String, ReservationData])

  override def behavior: Behavior = {
    case ReservationState(_, _) =>
      Actions()
        .onCommand[RequestReservation, Done] {

          // Command handler for the UseGreetingMessage command
          case (RequestReservation(reservationData), ctx, state) =>
            ctx.thenPersist(
              ReservationRequested(reservationData)
            ) { _ =>
              // Then once the event is successfully persisted, we respond with done.
              ctx.reply(Done)
            }
        }
        .onEvent {
          case (ReservationRequested(reservationData), state) =>
            val reservations = reservationData +: state.reservationRequests(
              reservationData.startingDate.toString)
            state.copy(
              reservationRequests = state.reservationRequests + ((reservationData.startingDate.toString,
                                                                  reservations)))
        }
  }
}
