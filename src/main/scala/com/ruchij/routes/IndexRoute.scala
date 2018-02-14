package com.ruchij.routes

import akka.http.scaladsl.server.Directives._
import com.ruchij.responses.ResponseUtils._
import com.ruchij.services.PingService

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
            onComplete(pingService.insert(clientIp))(tryResultHandler)
        }
      }
    } ~
    path("home-ip") {
      get {
        onComplete(pingService.getLatestIp())(tryResultHandler)
      }
    }
}
