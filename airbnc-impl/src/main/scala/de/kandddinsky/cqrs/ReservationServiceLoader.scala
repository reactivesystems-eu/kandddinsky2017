package de.kandddinsky.cqrs

import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.playjson.{
  JsonSerializer,
  JsonSerializerRegistry
}
import com.lightbend.lagom.scaladsl.server._
import com.softwaremill.macwire._
import play.api.libs.ws.ahc.AhcWSComponents

class ReservationServiceLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new AirbncApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new AirbncApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[ReservationService])
}

abstract class AirbncApplication(context: LagomApplicationContext)
    extends LagomApplication(context)
    with CassandraPersistenceComponents
    with LagomKafkaComponents
    with AhcWSComponents {

  // Bind the service that this server provides
  lazy val repository = wire[ReservationRepository]

  override lazy val lagomServer =
    serverFor[ReservationService](wire[ReservationServiceImpl])

  // Register the JSON serializer registry
  override lazy val jsonSerializerRegistry =
    ReservationServiceSerializerRegistry

  // Register the hello-lagom persistent entity
  persistentEntityRegistry.register(wire[ReservationAggregate])
  readSide.register[ReservationEvent](wire[ReservationProjector])

}

object ReservationServiceSerializerRegistry extends JsonSerializerRegistry {
  override def serializers: collection.immutable.Seq[JsonSerializer[_]] =
    collection.immutable.Seq(
      JsonSerializer[RequestReservation],
      JsonSerializer[ConfirmReservation],
      JsonSerializer[CancelReservation],
      JsonSerializer[ReservationRequested],
      JsonSerializer[ReservationRejected],
      JsonSerializer[ReservationConfirmed],
      JsonSerializer[ReservationCancelled]
    )
}
