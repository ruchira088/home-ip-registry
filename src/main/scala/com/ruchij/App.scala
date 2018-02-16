package com.ruchij

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.ruchij.authentication.SimpleAuthenticator
import com.ruchij.contants.{DefaultConfigValues, EnvVariableNames}
import com.ruchij.dao.SlickPingDao
import com.ruchij.ec.{DefaultMdcPropagatingContext, MdcPropagatingContext}
import com.ruchij.exceptions.UndefinedEnvVariableException
import com.ruchij.routes.IndexRoute
import com.ruchij.services.PingService
import com.ruchij.utils.ConfigUtils
import com.ruchij.utils.ConfigUtils.env
import com.ruchij.utils.ScalaUtils.parseInt
import com.typesafe.scalalogging.Logger

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future, Promise}
import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

object App
{
  val appLogger: Logger = Logger("MainApp")

  def main(args: Array[String]): Unit =
  {
    implicit val actorSystem: ActorSystem = ActorSystem("home-ip-registry")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: MdcPropagatingContext = DefaultMdcPropagatingContext(actorSystem.dispatcher)

    val slickPingDao: SlickPingDao = SlickPingDao()

    val server = for {
      result <- slickPingDao.createTableIfNonExistent()

      _ = appLogger.info {
        if (result)
          s"Created new table named ${SlickPingDao.TABLE_NAME}."
        else
          s"${SlickPingDao.TABLE_NAME} already exists in the database."
      }

      authSecret <- Try(ConfigUtils.env(EnvVariableNames.AUTH_SECRET).get)
        .fold(
          _ => Future.failed(UndefinedEnvVariableException(EnvVariableNames.AUTH_SECRET)),
          Future.successful
        )

      authenticationDirective = authenticateOAuth2PFAsync(SimpleAuthenticator.REALM, SimpleAuthenticator(authSecret))

      serverBinding <- Http().bindAndHandle(
        IndexRoute(PingService(slickPingDao),
          authenticationDirective
        ), serverAddress(), httpPort()
      )
    }
    yield serverBinding

    server.onComplete {
      case Success(_) => appLogger.info(s"Server (${serverAddress()}) is listening on port ${httpPort()}...")

      case Failure(NonFatal(throwable)) => {
        appLogger.error(s"ERROR !!! ${throwable.getMessage} ")
        System.exit(1)
      }
    }

    Await.ready(Promise[Unit].future, Duration.Inf)
  }

  def httpPort(): Int =
    env(EnvVariableNames.HTTP_PORT)
      .flatMap(parseInt(_).toOption)
      .getOrElse(DefaultConfigValues.HTTP_PORT)

  def serverAddress(): String =
    env(EnvVariableNames.SERVER_ADDRESS).getOrElse(DefaultConfigValues.SERVER_ADDRESS)
}
