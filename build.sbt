val ZioVersion            = "1.0.0-RC20"
val Specs2Version         = "4.7.0"
val Http4sVersion         = "0.21.3"
val CirceVersion          = "0.13.0"
val LogbackVersion        = "1.2.3"
val ZioInteropCatsVersion = "2.1.3.0-RC15"
val ZioLoggingVersion     = "0.3.0"
val ZioConfigVersion      = "1.0.0-RC20"
val PostgresqlVersion     = "42.2.12"
val DoobieVersion         = "0.9.0"
val FlywayVersion         = "6.4.3"
val CatsEffectVersion     = "2.1.3"

lazy val Http4sBlazeServer = "org.http4s"    %% "http4s-blaze-server" % Http4sVersion
lazy val Http4sClient      = "org.http4s"    %% "http4s-client"       % Http4sVersion
lazy val Http4sCirce       = "org.http4s"    %% "http4s-circe"        % Http4sVersion
lazy val Http4sDsl         = "org.http4s"    %% "http4s-dsl"          % Http4sVersion
lazy val CirceGeneric      = "io.circe"      %% "circe-generic"       % CirceVersion
lazy val Zio               = "dev.zio"       %% "zio"                 % ZioVersion
lazy val ZioInteropCats    = "dev.zio"       %% "zio-interop-cats"    % ZioInteropCatsVersion
lazy val Specs2Core        = "org.specs2"    %% "specs2-core"         % Specs2Version % Test
lazy val LogbackClassic    = "ch.qos.logback" % "logback-classic"     % LogbackVersion
lazy val ZioLogging        = "dev.zio"       %% "zio-logging"         % ZioLoggingVersion
lazy val ZioLoggingSlf4j   = "dev.zio"       %% "zio-logging-slf4j"   % ZioLoggingVersion
lazy val ZioConfig         = "dev.zio"       %% "zio-config"          % ZioConfigVersion
lazy val ZioConfigTypesafe = "dev.zio"       %% "zio-config-typesafe" % ZioConfigVersion
lazy val ZioConfigMagnolia = "dev.zio"       %% "zio-config-magnolia" % ZioConfigVersion
lazy val Postgresql        = "org.postgresql" % "postgresql"          % PostgresqlVersion
lazy val DoobieCore        = "org.tpolecat"  %% "doobie-core"         % DoobieVersion
lazy val DoobieHikari      = "org.tpolecat"  %% "doobie-hikari"       % DoobieVersion
lazy val DoobiePostgres    = "org.tpolecat"  %% "doobie-postgres"     % DoobieVersion
lazy val DoobieScalatest   = "org.tpolecat"  %% "doobie-scalatest"    % DoobieVersion % Test
lazy val FlywayCore        = "org.flywaydb"   % "flyway-core"         % FlywayVersion
lazy val CatsEffect        = "org.typelevel" %% "cats-effect"         % CatsEffectVersion

resolvers += Resolver.sonatypeRepo("releases")
resolvers += Resolver.sonatypeRepo("snapshots")

lazy val root = (project in file("."))
  .settings(
    organization := "io.github.socializator",
    name := "socializator",
    version := "0.0.1",
    scalaVersion := "2.13.2",
    maxErrors := 3,
    libraryDependencies ++= Seq(
      Zio,
      ZioInteropCats,
      ZioLogging,
      ZioLoggingSlf4j,
      ZioConfig,
      ZioConfigTypesafe,
      ZioConfigMagnolia,
      Specs2Core,
      CirceGeneric,
      Http4sBlazeServer,
      Http4sClient,
      Http4sCirce,
      Http4sDsl,
      LogbackClassic,
      Postgresql,
      DoobieCore,
      DoobieHikari,
      DoobiePostgres,
      DoobieScalatest,
      FlywayCore,
      CatsEffect,
      compilerPlugin(
        ("org.typelevel" % "kind-projector" % "0.11.0").cross(CrossVersion.full)
      )
    ),
    Compile / guardrailTasks += ScalaServer(
      baseDirectory.value / "apispec.yaml",
      "io.github.socializator.generated.server",
      framework = "http4s"
    )
  )

// Refine scalac params from tpolecat
scalacOptions --= Seq(
  "-feature",
  "-deprecation",
  "-explaintypes",
  "-unchecked",
  "-encoding",
  "UTF-8",
  "-language:higherKinds",
  "-language:existentials",
  "-Xfatal-warnings",
  "-Xlint:-infer-any,_",
  "-Ywarn-value-discard",
  "-Ywarn-numeric-widen",
  "-Ywarn-extra-implicit",
  "-Ywarn-unused:_"
)
