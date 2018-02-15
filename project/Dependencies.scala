import sbt._

object Dependencies
{
  val AKKA_HTTP_VERSION = "10.0.11"
  lazy val akkaHttpSprayJson = "com.typesafe.akka" %% "akka-http-spray-json" % AKKA_HTTP_VERSION
  lazy val akkaHttp = "com.typesafe.akka" %% "akka-http" % AKKA_HTTP_VERSION

  val AKKA_VERSION = "2.5.9"
  lazy val akkaStream = "com.typesafe.akka" %% "akka-stream" % AKKA_VERSION
  lazy val akkaActor = "com.typesafe.akka" %% "akka-actor" % AKKA_VERSION

  val SLICK_VERSION = "3.2.1"
  lazy val slick = "com.typesafe.slick" %% "slick" % SLICK_VERSION
  lazy val slickHikariCp = "com.typesafe.slick" %% "slick-hikaricp" % SLICK_VERSION
  lazy val sqliteDriver = "org.xerial" % "sqlite-jdbc" % "3.21.0.1"

  lazy val jodaTime = "joda-time" % "joda-time" % "2.9.9"

  lazy val logBack = "ch.qos.logback" % "logback-classic" % "1.2.3"
  lazy val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2"

  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.4"
  lazy val pegdown = "org.pegdown" % "pegdown" % "1.6.0"
}