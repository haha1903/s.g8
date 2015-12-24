organization := "$organization$"

name := "$name$"

version := "$version$"

scalaVersion := "$scala_version$"

scalacOptions ++= Seq("-deprecation", "-feature")

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % "$scalatra_version$",
  "org.scalatra" %% "scalatra-swagger" % "$scalatra_version$",
  "org.scalatra" %% "scalatra-json" % "$scalatra_version$",
  "org.json4s" %% "json4s-jackson" % "3.3.0",
  "com.typesafe.slick" %% "slick" % "$slick_version$",
  "mysql" % "mysql-connector-java" % "5.1.34",
  "com.typesafe.slick" %% "slick-codegen" % "$slick_version$",
  "org.scalatra" %% "scalatra-specs2" % "$scalatra_version$" % "test",
  "ch.qos.logback" % "logback-classic" % "1.1.2" % "runtime",
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided"
)

enablePlugins(JettyPlugin)

javaOptions in Jetty ++= Seq(
  "-Xdebug",
  "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
)