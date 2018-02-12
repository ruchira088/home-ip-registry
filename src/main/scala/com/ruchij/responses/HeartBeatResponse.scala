package com.ruchij.responses

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.RemoteAddress
import spray.json.DefaultJsonProtocol

case class HeartBeatResponse(ipAddress: Option[String])

object HeartBeatResponse extends DefaultJsonProtocol with SprayJsonSupport
{
  def create(remoteAddress: RemoteAddress) = HeartBeatResponse(remoteAddress.toIP.map(_.ip.getHostAddress))

  implicit def jsonFormatter = jsonFormat1(HeartBeatResponse.apply)
}
