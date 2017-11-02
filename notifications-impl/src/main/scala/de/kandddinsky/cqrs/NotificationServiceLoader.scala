package de.kandddinsky.cqrs

import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaClientComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.server._
import com.softwaremill.macwire._
import play.api.libs.ws.ahc.AhcWSComponents

class NotificationServiceLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new AirbncNotificationApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new AirbncNotificationApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[ReservationService])
}

abstract class AirbncNotificationApplication(context: LagomApplicationContext)
    extends LagomApplication(context)
    with LagomKafkaClientComponents
    with AhcWSComponents {

  lazy val reservationService = serviceClient.implement[ReservationService]

  // Bind the service that this server provides
  override lazy val lagomServer =
    serverFor[NotificationService](wire[NotificationServiceImpl])

}
