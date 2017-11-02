package de.kandddinsky.cqrs

import akka.Done
import akka.stream.scaladsl.Flow
import com.lightbend.lagom.scaladsl.api.ServiceCall
import org.slf4j.LoggerFactory

import scala.concurrent.Future

class NotificationServiceImpl(reservationService: ReservationService)
    extends NotificationService {

  private val log = LoggerFactory.getLogger(classOf[NotificationServiceImpl])

  private val messages = reservationService.reservationNotifications

  messages.subscribe
    .withGroupId("notification-service")
    .atLeastOnce(Flow[ReservationNotification].mapAsync(1) { msg =>
      sendNotification(msg)
    })

  def sendNotification(notification: ReservationNotification) = {
    log.info(s"MESSAGE RECEIVED: ${notification.toString}")
    Future.successful(Done)
  }

  override def healthCheck() = ServiceCall { request =>
    Future.successful("OK")
  }
}
