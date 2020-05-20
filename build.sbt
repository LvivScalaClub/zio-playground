val ZioVersion = "1.0.0-RC19-2"
val Specs2Version = "4.7.0"
val http4sVersion = "0.21.3"
val circeVersion = "0.13.0"

lazy val http4s = "org.http4s" %% "http4s-blaze-server" % http4sVersion
lazy val http4sBlazeClient = "org.http4s" %% "http4s-blaze-client" % http4sVersion
lazy val http4sCirce = "org.http4s" %% "http4s-circe" % http4sVersion
lazy val http4sDsl = "org.http4s" %% "http4s-dsl" % http4sVersion
lazy val circeGeneric = "io.circe" %% "circe-generic" % circeVersion

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
      "dev.zio" %% "zio" % ZioVersion,
      "org.specs2" %% "specs2-core" % Specs2Version % "test",
      circeGeneric,
      http4s,
      http4sBlazeClient,
      http4sCirce,
      http4sDsl
    ),
    Compile / guardrailTasks += ScalaServer(
      baseDirectory.value / "openapi.yaml",
      "com.scalaclub.server.external",
      framework = "http4s"
    )
  )

// Refine scalac params from tpolecat
scalacOptions --= Seq(
  "-Xfatal-warnings"
)
