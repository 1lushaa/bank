ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.4.3"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-effect" % "3.5.7",
  "org.tpolecat" %% "doobie-core" % "1.0.0-RC4",
  "org.tpolecat" %% "doobie-postgres" % "1.0.0-RC4",
  "org.tpolecat" %% "doobie-scalatest" % "1.0.0-RC4" % "test",
  "com.softwaremill.sttp.tapir" %% "tapir-core" % "1.11.11",
  "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % "1.11.11",
  "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % "1.4.0",
  "org.http4s" %% "http4s-ember-server" % "0.23.23"
)

lazy val root = (project in file("."))
  .settings(
    name := "Bank"
  )
