name := """vielen-games-api"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  filters,
  "se.radley" %% "play-plugins-salat" % "1.5.0",
  "com.restfb" % "restfb" % "1.6.12"
)
