package com.ruchij.utils

import java.util.UUID

import akka.http.scaladsl.model.RemoteAddress

object GeneralUtils
{
  def uuid(): String = UUID.randomUUID().toString

  def shortId() = uuid().substring(0, 8)

  def ip(remoteAddress: RemoteAddress) = remoteAddress.toIP.fold("UNKNOWN")(_.ip.getHostAddress)
}
