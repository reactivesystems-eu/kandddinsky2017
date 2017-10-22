package de.kandddinsky.cqrs

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.api.broker.kafka.{
  KafkaProperties,
  PartitionKeyStrategy
}
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}
import play.api.libs.json.{Format, Json}

/**
  * The hello-lagom service interface.
  * <p>
  * This describes everything that Lagom needs to know about how to serve and
  * consume the HellolagomService.
  */
trait ReservationService extends Service {

  /**
    * Example: curl -H "Content-Type: application/json" -X POST -d '{"message":
    * "Hi"}' http://localhost:9000/api/hello/Alice
    */
  def requestReservation(accomodation: String): ServiceCall[Reservation, Done]

  override final def descriptor = {
    import Service._
    // @formatter:off
    named("airbnc")
      .withCalls(
        restCall(Method.POST, "/api/accomodation/:accomodation/reservation", requestReservation _)
      )
      .withAutoAcl(true)
    // @formatter:on
  }
}
