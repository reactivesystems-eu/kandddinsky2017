package de.kandddinsky.cqrs

import java.time.LocalDate

import play.api.libs.json.Json.JsValueWrapper
import play.api.libs.json._

/**
  */
object MapFormats {
  trait ToString[A] {
    def toStringValue(v: A): String
  }

  trait FromString[A] {
    def fromString(v: String): A
  }

  implicit val localDateToString = new ToString[LocalDate] {
    override def toStringValue(v: LocalDate): String = v.toString
  }

  implicit val localDateFromString = new FromString[LocalDate] {
    override def fromString(v: String): LocalDate = LocalDate.parse(v)
  }

  def mapReads[K, V: Reads](implicit fstc: FromString[K]): Reads[Map[K, V]] =
    new Reads[Map[K, V]] {
      def reads(jv: JsValue): JsResult[Map[K, V]] =
        JsSuccess(
          jv.as[Map[String, V]].map { case (k, v) => fstc.fromString(k) -> v })
    }

  def mapWrites[K, V: Writes](implicit tstc: ToString[K]): Writes[Map[K, V]] =
    new Writes[Map[K, V]] {
      def writes(map: Map[K, V]): JsValue =
        Json.obj(map.map {
          case (s, o) =>
            val ret: (String, JsValueWrapper) = tstc.toStringValue(s) -> o
            ret
        }.toSeq: _*)
    }

  implicit def mapFormat[K, V: Format](implicit tstc: ToString[K],
                                       fstc: FromString[K]): Format[Map[K, V]] =
    Format(mapReads, mapWrites)

}
