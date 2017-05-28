enablePlugins(JavaAppPackaging)

name := "roshambo"
organization := "org.ebay"
version := "1.0.0"

scalaVersion := "2.12.2"
	
scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-Xfatal-warnings",
  "-Xlint:missing-interpolator",
  "-Ywarn-unused-import",
  "-Ywarn-unused",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen")

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.2.13",
  "org.scalaz" %% "scalaz-effect" % "7.2.13",
  "org.scalatest" %% "scalatest" % "3.0.3" % "test",
  "junit" % "junit" % "4.10" % "test",
  "org.scalacheck" %% "scalacheck" % "1.13.5" % "test",
  "org.scalaz" %% "scalaz-scalacheck-binding" % "7.2.13" % "test")
