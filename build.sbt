val ZioVersion = "1.0.0-RC20"
val Specs2Version = "4.7.0"
val Http4sVersion = "0.21.3"
val CirceVersion = "0.13.0"
val LogbackVersion = "1.2.3"
val ZioInteropCatsVersion = "2.1.3.0-RC15"
val PureconfigVersion = "0.12.3"
val ZioLoggingVersion = "0.3.0"

lazy val Http4sBlazeServer = "org.http4s" %% "http4s-blaze-server" % Http4sVersion
lazy val Http4sClient = "org.http4s" %% "http4s-client" % Http4sVersion
lazy val Http4sCirce = "org.http4s" %% "http4s-circe" % Http4sVersion
lazy val Http4sDsl = "org.http4s" %% "http4s-dsl" % Http4sVersion
lazy val CirceGeneric = "io.circe" %% "circe-generic" % CirceVersion
lazy val Zio =  "dev.zio" %% "zio" % ZioVersion
lazy val ZioInteropCats =  "dev.zio" %% "zio-interop-cats" % ZioInteropCatsVersion
lazy val Specs2Core = "org.specs2" %% "specs2-core" % Specs2Version % "test"
lazy val LogbackClassic = "ch.qos.logback"  %  "logback-classic"  % LogbackVersion
lazy val Pureconfig = "com.github.pureconfig" %% "pureconfig" % PureconfigVersion
lazy val ZioLogging = "dev.zio" %% "zio-logging" % ZioLoggingVersion
lazy val ZioLoggingSlf4j = "dev.zio" %% "zio-logging-slf4j" % ZioLoggingVersion

resolvers += Resolver.sonatypeRepo("releases")
resolvers += Resolver.sonatypeRepo("snapshots")

lazy val root = (project in file("."))
  .settings(
    organization := "lvivscalaclub",
    name := "zio-playground",
    version := "0.0.1",
    scalaVersion := "2.13.2",
    maxErrors := 3,
    libraryDependencies ++= Seq(
      Zio,
      ZioInteropCats,
      ZioLogging,
      ZioLoggingSlf4j,
      Specs2Core,
      CirceGeneric,
      Http4sBlazeServer,
      Http4sClient,
      Http4sCirce,
      Http4sDsl,
      LogbackClassic,
      Pureconfig
    ),
    Compile / guardrailTasks += ScalaServer(
      baseDirectory.value / "openapi.yaml",
      "io.github.socializator.generated.server",
      framework = "http4s"
    )
  )

// Refine scalac params from tpolecat
scalacOptions --= Seq(
  "-Xfatal-warnings"
)
