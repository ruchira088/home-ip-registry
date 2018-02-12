package com.ruchij.exceptions

case class UndefinedEnvVariableException(envName: String) extends Exception
{
  override def getMessage: String = s""""$envName" is NOT defined in the environment."""
}
