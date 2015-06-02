import com.earldouglas.xwp.XwpPlugin._
import net.virtualvoid.sbt.graph.Plugin.graphSettings
import sbt.Keys._
import sbt._
import sbtprofile.Plugin._
import xerial.sbt.Pack._

object $name;format="Camel"$Build extends Build {
  val Organization = "$organization$"
  val Name = "$name$"
  val Version = "$version$"
  val ScalaVersion = "$scala_version$"
  val ScalatraVersion = "$scalatra_version$"
  val SlickVersion = "$slick_version$"
  val JettyVersion = "9.2.11.v20150529"

  lazy val project = Project(
    "$name;format="norm"$",
    file("."),
    settings = graphSettings ++ jetty() ++ packAutoSettings ++ profileSettings ++ Seq(
      organization := Organization,
      name := Name,
      version := Version,
      scalaVersion := ScalaVersion,
      scalacOptions ++= Seq("-deprecation", "-feature"),
      resolvers ++= Seq("nexus-m2" at "http://cq01-rdqa-pool106.cq01.baidu.com:8081/nexus/content/groups/public/",
        Resolver.url("nexus-ivy", url("http://cq01-rdqa-pool106.cq01.baidu.com:8081/nexus/content/groups/public/"))(Resolver.ivyStylePatterns)),
      resourceGenerators in Compile <+= (resourceManaged, baseDirectory) map {
        (managedBase, base) =>
          val webappBase = base / "src" / "main" / "webapp"
          for {
            (from, to) <- webappBase ** "*" pair rebase(webappBase, managedBase / "main" / "webapp")
          } yield {
            Sync.copy(from, to)
            to
          }
      },
      javaOptions in container ++= Seq(
        "-Xdebug",
        "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
      ),
      libraryDependencies ++= Seq(
        "org.scalatra" %% "scalatra" % ScalatraVersion,
        "org.scalatra" %% "scalatra-swagger" % ScalatraVersion,
        "org.scalatra" %% "scalatra-json" % ScalatraVersion,
        "org.json4s" %% "json4s-jackson" % "3.3.0.RC2",
        "com.typesafe.slick" %% "slick" % SlickVersion,
        "mysql" % "mysql-connector-java" % "5.1.34",
        "com.typesafe.slick" %% "slick-codegen" % SlickVersion,
        "org.scalatra" %% "scalatra-specs2" % ScalatraVersion % "test",
        "ch.qos.logback" % "logback-classic" % "1.1.2" % "runtime",
        "org.eclipse.jetty" % "jetty-webapp" % JettyVersion % "container",
        "org.eclipse.jetty" % "jetty-plus" % JettyVersion % "container",
        "javax.servlet" % "javax.servlet-api" % "3.1.0"
      )
    )
  )
}
