name := "scala-learning"

version := "0.1"

scalaVersion := "2.13.8"

libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.3.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.12" % "test"
// https://mvnrepository.com/artifact/net.minidev/json-smart
libraryDependencies += "net.minidev" % "json-smart" % "2.3"
libraryDependencies += "org.typelevel" %% "cats-effect" % "3.3.12"
libraryDependencies += "org.typelevel" %% "cats-core" % "2.7.0"
libraryDependencies += "com.chuusai" %% "shapeless" % "2.3.3"
libraryDependencies += "org.jetbrains.kotlin" % "kotlin-reflect" % "1.8.20-Beta"


val circeVersion = "0.14.1"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)