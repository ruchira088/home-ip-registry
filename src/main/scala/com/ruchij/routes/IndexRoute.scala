package com.ruchij.routes

import akka.http.scaladsl.server.Directives._
import com.ruchij.responses.HeartBeatResponse

object IndexRoute
{
  def apply() =
    path("") {
      get {
        complete("Hello World")
      }
    } ~
    path("heart-beat") {
      post {
        extractClientIP {
          clientIp => complete(HeartBeatResponse.create(clientIp))
        }
      }
    }
}
