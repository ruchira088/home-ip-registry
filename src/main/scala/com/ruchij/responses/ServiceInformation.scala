package com.ruchij.responses

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.{Version => akkaVersion}
import akka.http.{Version => akkaHttpVersion}
import spray.json.DefaultJsonProtocol

import scala.util.Properties

case class ServiceInformation private (
   serviceName: String,
   scalaVersion: String,
   javaVersion: String,
   akkaVersion: String,
   akkaHttpVersion: String
)

object ServiceInformation extends DefaultJsonProtocol with SprayJsonSupport
{
  val SERVICE_NAME = "home-ip-registry"

  def fetch() = ServiceInformation(
    SERVICE_NAME,
    Properties.versionNumberString,
    Properties.javaVersion,
    akkaVersion.current,
    akkaHttpVersion.current
  )

  implicit def jsonFormatter = jsonFormat5(ServiceInformation.apply)
}