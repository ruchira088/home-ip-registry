package com.ruchij.exceptions

object MissingAuthTokenException extends Exception
{
  override def getMessage: String = "Missing authentication token."
}
