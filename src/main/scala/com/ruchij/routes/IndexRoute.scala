package com.ruchij.routes

import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.server.Directives._
import com.ruchij.services.PingService

import scala.util.{Failure, Success}

object IndexRoute
{
  def apply(pingService: PingService) =
    path("") {
      get {
        complete("Hello World")
      }
    } ~
    path("heart-beat") {
      post {
        extractClientIP {
          clientIp =>
            onComplete(pingService.insert(clientIp))
            {
              case Success(ping) => complete(ping)
              case Failure(exception) => complete(ToResponseMarshallable(exception))
            }
        }
      }
    }
}
