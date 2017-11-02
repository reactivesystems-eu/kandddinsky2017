package de.kandddinsky.cqrs

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.Service._
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}

trait NotificationService extends Service {
  def healthCheck(): ServiceCall[NotUsed, String]

  override final def descriptor: Descriptor =
    named("notifications")
      .withCalls(
        restCall(Method.GET, "/api/notifications/health", healthCheck)
      )
      .withAutoAcl(true)
}
