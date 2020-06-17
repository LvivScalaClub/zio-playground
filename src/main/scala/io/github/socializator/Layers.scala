package io.github.socializator

import zio._
import zio.blocking.Blocking
import zio.logging.Logging
import zio.config.{config, Config}
import io.github.socializator.configuration._
import io.github.socializator.logging.AppLogging
import io.github.socializator.database._
import doobie.util.transactor.Transactor

object Layers {
  type AppEnv = Config[AppConfig]
    with Logging
    with Has[DatabaseConfig]
    with Has[ApiConfig]
    with Has[Transactor[Task]]
    with Has[PetsRepository.Service]

  object live {
    val databaseConfig = Configuration.live >>> database
    val apiConfig      = Configuration.live >>> api
    val transactor     = (Blocking.any ++ databaseConfig) >>> AppTransactor.live
    val petsRepository = (transactor ++ doobiePostgresContext) >>> PetsRepository.live

    val AppLayer: ZLayer[Blocking, Throwable, AppEnv] = {
      Blocking.any >>> (Configuration.live ++ AppLogging.live ++ databaseConfig ++ apiConfig ++ transactor ++ petsRepository)
    }
  }
}
