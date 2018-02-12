package com.ruchij.services

import akka.http.scaladsl.model.RemoteAddress
import com.ruchij.dao.PingDao
import com.ruchij.models.Ping
import com.ruchij.utils.GeneralUtils.uuid
import org.joda.time.DateTime

import scala.concurrent.Future

class PingService(pingDao: PingDao)
{
  def insert(remoteAddress: RemoteAddress): Future[Ping] =
    pingDao.insert {
      Ping(uuid(), DateTime.now(), remoteAddress.toString())
    }

  def getLatestIp(): Future[Ping] = pingDao.getLatestPing()
}
