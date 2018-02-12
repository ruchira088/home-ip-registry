package com.ruchij.exceptions

object EmptyDatabaseException extends Exception
{
  override def getMessage: String = "Database table is EMPTY"
}
