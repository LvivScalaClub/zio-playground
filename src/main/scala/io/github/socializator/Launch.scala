package io.github.socializator

import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.CORS
import zio._
import zio.blocking.Blocking
import zio.clock.Clock
import zio.console.putStrLn
import zio.interop.catz._
import cats.effect.{ExitCode => CatsExitCode}
import io.github.socializator.configuration.Configuration
import io.github.socializator.generated.server.pets.PetsResource
import io.github.socializator.controller.PetsController
import zio.logging._
import zio.logging.slf4j._
import io.github.socializator.configuration.ApiConfig

object Launch extends zio.App {
  type AppEnvironment = Configuration with Clock
  type AppTask[A]     = RIO[AppEnvironment, A]

  val liveLogger =
    Slf4jLogger.make(
      logFormat = (context, line) => line,
      rootLoggerName = None
    )

  override def run(args: List[String]): ZIO[ZEnv, Nothing, zio.ExitCode] = {

    val program = for {
      apiConfig <- configuration.apiConfig
      _ <- log.info(
        s"Starting server at http://${apiConfig.host}:${apiConfig.port} ..."
      )
      server <- runHttpServer(apiConfig)
    } yield server

    program
      .provideSomeLayer[ZEnv](Configuration.live ++ liveLogger)
      .tapError(err => putStrLn(s"Execution failed with: $err"))
      .exitCode
  }

  private def runHttpServer(apiConfig: ApiConfig): ZIO[AppEnvironment, Throwable, Unit] = {
    val httpApp = (
      new PetsResource[AppTask]().routes(new PetsController[AppTask]())
    ).orNotFound

    ZIO.runtime[AppEnvironment].flatMap { implicit rts =>
      BlazeServerBuilder[AppTask]
        .bindHttp(apiConfig.port, apiConfig.host)
        .withHttpApp(CORS(httpApp))
        .serve
        .compile[AppTask, AppTask, CatsExitCode]
        .drain
    }
  }
}
