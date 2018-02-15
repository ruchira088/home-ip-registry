package com.ruchij.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.AuthenticationDirective
import com.ruchij.exceptions.EmptyDatabaseException
import com.ruchij.responses.ResponseUtils._
import com.ruchij.services.PingService

object IndexRoute
{
  def apply(pingService: PingService, authentication: AuthenticationDirective[_]) =
    handleExceptions(exceptionHandler) {
      path("") {
        get {
          complete("Hello World")
        }
      } ~
        path("heart-beat") {
          post {
            authentication { _ =>
              extractClientIP {
                clientIp =>
                  onComplete(pingService.insert(clientIp)) {
                    tryResultHandler()
                  }
              }
            }
          }
        } ~
        path("home-ip") {
          get {
            authentication { _ =>
              onComplete(pingService.getLatestIp()) {
                tryResultHandler {
                  case EmptyDatabaseException => complete(StatusCodes.NoContent)
                }
              }
            }
          }
        }
    }
}
