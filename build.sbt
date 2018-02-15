import Dependencies._

lazy val root = (project in file("."))
  .settings(
    inThisBuild(List(
      organization := "com.ruchij",
      scalaVersion := "2.12.4"
    )),
    name := "home-ip-registry",
    libraryDependencies ++= Seq(
      akkaHttpSprayJson,
      akkaHttp,
      akkaStream,
      akkaActor,
      jodaTime,
      slick,
      slickHikariCp,
      sqliteDriver,
      logBack,
      scalaLogging,

      scalaTest % Test,
      pegdown % Test
    )
  )

coverageEnabled := true

testOptions in Test +=
  Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-results")

addCommandAlias("testWithCoverage", "; clean; test; coverageReport")