package de.kandddinsky.cqrs

import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType

/**
  * hello-lagom
  */
sealed trait ReservationCommand[R] extends ReplyType[R] {

}
