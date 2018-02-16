// The simplest possible sbt build file is just one line:

scalaVersion := "2.12.4"

name := "twitter4s-fs2"
organization := "com.danielasfregola"
version := "5.4-SNAPSHOT"

// maintainer := "Peter Becich <peterbecich@gmail.com>"

val AkkaHttpJson4s = "1.20.0-RC2"
val FS2 = "0.10.1"
val Twitter4s = "5.4"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "1.0.1",
  "de.heikoseeberger" %% "akka-http-circe" % AkkaHttpJson4s,
  "co.fs2" %% "fs2-core" % FS2,
  "com.danielasfregola" %% "twitter4s" % Twitter4s
)

scalacOptions += "-Ypartial-unification"
