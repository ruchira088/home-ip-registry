package com.ruchij.authentication

import akka.http.scaladsl.server.directives.Credentials
import akka.http.scaladsl.server.directives.Credentials._
import com.ruchij.exceptions.{InvalidAuthTokenException, MissingAuthTokenException}

import scala.concurrent.Future

object SimpleAuthenticator 
{
  val REALM = "all-users"

  def apply(secret: String): PartialFunction[Credentials, Future[Unit]] = {
    case Missing => Future.failed(MissingAuthTokenException)
    case Provided(token) =>
      if (token == secret) Future.successful((): Unit) else Future.failed(InvalidAuthTokenException)
  }
}
