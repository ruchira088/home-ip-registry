package com.ruchij.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.AuthenticationDirective
import com.ruchij.exceptions.EmptyDatabaseException
import com.ruchij.responses.ResponseUtils._
import com.ruchij.responses.ServiceInformation
import com.ruchij.services.PingService
import com.ruchij.utils.GeneralUtils._
import com.typesafe.scalalogging.Logger
import org.slf4j.MDC

object IndexRoute
{
  val indexRouteLogger = Logger("IndexRoute")

  def apply(pingService: PingService, authentication: AuthenticationDirective[_]) =
    handleExceptions(exceptionHandler) {
      MDC.put("requestId", shortId())
      path("") {
        get {
          complete(ServiceInformation.fetch())
        }
      } ~
      path("heart-beat") {
        post {
          extractClientIP {
            clientIp => {
              MDC.put("ip", ip(clientIp))
              authentication { _ =>
                onComplete(pingService.insert(clientIp)) {
                  tryResultHandler()
                }
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
