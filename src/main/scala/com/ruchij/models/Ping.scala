package com.ruchij.models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import org.joda.time.DateTime
import spray.json.{DefaultJsonProtocol, DeserializationException, JsString, JsValue, RootJsonFormat}

import scala.util.control.NonFatal

case class Ping(id: String, timestamp: DateTime, ip: String)

object Ping extends SprayJsonSupport with DefaultJsonProtocol
{
  implicit def dateTimeFormat: RootJsonFormat[DateTime] = new RootJsonFormat[DateTime]
  {
    override def write(dateTime: DateTime): JsValue = JsString(dateTime.toString)

    override def read(jsValue: JsValue): DateTime =
      try {
        DateTime.parse(jsValue.convertTo[String])
      } catch {
        case NonFatal(throwable) => throw DeserializationException(throwable.getMessage)
      }
  }

  implicit def jsonFormatter: RootJsonFormat[Ping] = jsonFormat3(Ping.apply)
}
