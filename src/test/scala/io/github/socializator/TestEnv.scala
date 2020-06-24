package io.github.socializator

import io.github.socializator.Layers.AppEnv
import io.github.socializator.configuration._
import io.github.socializator.configuration.Configuration
import io.github.socializator.database.{AppTransactor, PetsRepository}
import io.github.socializator.logging.AppLogging

import zio.ZLayer
import zio.blocking.Blocking

object TestEnv {
  val databaseConfig = Configuration.live >>> database
  val apiConfig      = Configuration.live >>> api
  val transactor     = (Blocking.any ++ databaseConfig) >>> AppTransactor.test
  val petsRepository = (transactor ++ doobiePostgresContext) >>> PetsRepository.live

  val AppLayer: ZLayer[Blocking, Throwable, AppEnv] = {
    Blocking.any >>> (Configuration.live ++ AppLogging.live ++ databaseConfig ++ apiConfig ++ transactor ++ petsRepository)
  }
}
