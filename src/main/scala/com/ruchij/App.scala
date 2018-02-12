package com.ruchij

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.ruchij.contants.{DefaultConfigValues, EnvVariableNames}
import com.ruchij.routes.IndexRoute
import com.ruchij.utils.ConfigUtils.env
import com.ruchij.utils.ScalaUtils.parseInt

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContextExecutor, Promise}
import scala.util.control.NonFatal
import scala.util.{Failure, Success}

object App
{
  def main(args: Array[String]): Unit =
  {
    implicit val actorSystem: ActorSystem = ActorSystem("home-ip-registry")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher

    Http().bindAndHandle(IndexRoute(), serverAddress(), httpPort())
        .onComplete
        {
          case Success(_) => println(s"Server (${serverAddress()}) is listening on port ${httpPort()}...")

          case Failure(NonFatal(throwable)) => {
            System.err.println(s"ERROR !!! ${throwable.getMessage} ")
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
