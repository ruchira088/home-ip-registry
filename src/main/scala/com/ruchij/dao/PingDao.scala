package com.ruchij.dao

import com.ruchij.models.Ping

import scala.concurrent.Future

trait PingDao
{
  def insert(ping: Ping): Future[Ping]

  def getLatestPing(): Future[Ping]
}
