package com.ruchij.responses

import akka.http.scaladsl.marshalling.{Marshaller, ToResponseMarshallable, ToResponseMarshaller}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route, StandardRoute}

import scala.util.Try
import scala.util.control.NonFatal

object ResponseUtils
{
  implicit def exceptionMarshaller: Marshaller[Throwable, HttpResponse] =
    Marshaller.opaque {
      case NonFatal(throwable) =>
        HttpResponse(
          status = StatusCodes.InternalServerError,
          entity = HttpEntity(ContentTypes.`application/json`, s"""{"error": "${throwable.getMessage}"}""")
        )
    }

  def tryResultHandler[A](result: Try[A])(implicit toResponseMarshaller: ToResponseMarshaller[A]): Route =
    result.fold[Route](
      {
        case NonFatal(throwable) =>
          complete(ToResponseMarshallable(throwable))
      },
      value => complete(ToResponseMarshallable(value))
    )

}
