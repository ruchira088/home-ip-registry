package com.ruchij.services

import akka.http.scaladsl.model.RemoteAddress
import com.ruchij.dao.PingDao
import com.ruchij.models.Ping
import com.ruchij.utils.GeneralUtils._
import com.typesafe.scalalogging.Logger
import org.joda.time.DateTime

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal
import scala.util.{Failure, Success}

class PingService(pingDao: PingDao)(implicit executionContext: ExecutionContext)
{
  val pingServiceLogger: Logger = Logger[PingService]

  def insert(remoteAddress: RemoteAddress): Future[Ping] =
    pingDao
      .insert {
        Ping(uuid(), DateTime.now(), ip(remoteAddress))
      }
      .andThen {
        case Success(Ping(_, _, ip)) => pingServiceLogger.info(s"Received ping from $ip")
        case Failure(NonFatal(throwable)) => pingServiceLogger.warn(throwable.getMessage)
      }

  def getLatestIp(): Future[Ping] = pingDao.getLatestPing()
}

object PingService
{
  def apply(pingDao: PingDao)(implicit executionContext: ExecutionContext): PingService = new PingService(pingDao)
}