package com.ruchij.responses

import spray.json.{JsString, JsonWriter}

case class ErrorResponse(exception: Throwable)

object ErrorResponse
{
  implicit def jsonWriter: JsonWriter[ErrorResponse] =
    (errorResponse: ErrorResponse) => JsString(s"""{""error": ${errorResponse.exception}""")
}
