package com.ruchij.utils

import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

object ScalaUtils
{
  def transform[A, B](transformer: A => B)(value: A): Try[B] =
    try {
      Success(transformer(value))
    }
    catch {
      case NonFatal(throwable) => Failure(throwable)
    }

  val parseInt: String => Try[Int] = transform[String, Int](_.toInt)
}
