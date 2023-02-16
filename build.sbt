ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.11" % Test
libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2"
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.6.17"


lazy val root = (project in file("."))
  .settings(
    name := "Laborator_PTR"
  )
