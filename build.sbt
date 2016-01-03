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
  "com.novus" %% "salat" % "1.9.9",
  "com.restfb" % "restfb" % "1.6.12",
  "org.mongodb" %% "casbah" % "3.0.0"
)
