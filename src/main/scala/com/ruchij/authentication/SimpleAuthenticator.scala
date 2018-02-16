package com.ruchij.authentication

import akka.http.scaladsl.server.directives.Credentials
import akka.http.scaladsl.server.directives.Credentials._
import com.ruchij.exceptions.{InvalidAuthTokenException, MissingAuthTokenException}
import com.typesafe.scalalogging.Logger

import scala.concurrent.{ExecutionContext, Future}

object SimpleAuthenticator 
{
  val REALM = "all-users"

  val simpleAuthenticatorLogger = Logger("SimpleAuthenticator")

  def apply(secret: String)(implicit executionContext: ExecutionContext): PartialFunction[Credentials, Future[Unit]] = {
    case Missing => Future.failed(MissingAuthTokenException)
      .andThen { case  _ => simpleAuthenticatorLogger.info(MissingAuthTokenException.getMessage) }
    case Provided(token) =>
      if (token == secret)
        Future.successful((): Unit)
          .andThen {
            case _ => simpleAuthenticatorLogger.info("Authentication success.")
          }
      else
        Future.failed(InvalidAuthTokenException)
          .andThen {
            case _ => simpleAuthenticatorLogger.info(InvalidAuthTokenException.getMessage)
          }
  }
}
