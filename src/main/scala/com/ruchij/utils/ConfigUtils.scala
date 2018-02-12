package com.ruchij.utils

object ConfigUtils
{
  def env(name: String): Option[String] =
    sys.env.get(name)
}
