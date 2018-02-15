package com.ruchij.responses

import akka.http.scaladsl.marshalling.{Marshaller, ToResponseMarshallable, ToResponseMarshaller}
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ExceptionHandler, Route, StandardRoute}
import com.ruchij.exceptions.{InvalidAuthTokenException, MissingAuthTokenException}

import scala.util.Try
import scala.util.control.NonFatal

object ResponseUtils
{
  private def errorResponse(statusCode: StatusCode, throwable: Throwable): StandardRoute =
    complete {
      HttpResponse(
        status = statusCode,
        entity = HttpEntity(ContentTypes.`application/json`, s"""{"error": "${throwable.getMessage}"}""")
      )
    }

  def exceptionHandler = ExceptionHandler {
    case MissingAuthTokenException => errorResponse(StatusCodes.Unauthorized, MissingAuthTokenException)
    case InvalidAuthTokenException => errorResponse(StatusCodes.Unauthorized, InvalidAuthTokenException)
  }

  def tryResultHandler[A](errorHandler: PartialFunction[Throwable, Route] = PartialFunction.empty)(result: Try[A])(implicit toResponseMarshaller: ToResponseMarshaller[A]): Route =
    result.fold[Route](
      errorHandler.orElse {
        case NonFatal(throwable) =>
          errorResponse(StatusCodes.InternalServerError, throwable)
      }
     ,
      value => complete(ToResponseMarshallable(value))
    )
}
