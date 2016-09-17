import scala.language.postfixOps

organization := "eu.kedev"

name := "auth-lib"

version := "0.0.14"

scalaVersion in ThisBuild := "2.11.7"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % "2.5.3",
  "org.slf4j" % "slf4j-api" % "1.7.12",
  "com.google.inject" % "guice" % "4.0",
  "net.codingwell" %% "scala-guice" % "4.0.0",
  "com.typesafe.play" %% "play" % "2.5.3" % Provided,
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % Provided,
  "com.vaadin" % "vaadin-server" % "7.5.3" % Provided,
  "org.scalatest"  %% "scalatest" % "2.2.4" % Test,
  "io.spray" %% "spray-routing" % "1.3.2" % Provided
)
