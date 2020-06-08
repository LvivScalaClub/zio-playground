package io.github.socializator

import zio._
import zio.config.{config, Config}
import zio.config.typesafe._
import zio.config.syntax._
import zio.config.magnolia.DeriveConfigDescriptor

package object configuration {
  case class AppConfig(api: ApiConfig, database: DatabaseConfig)
  case class ApiConfig(host: String, port: Int)
  case class DatabaseConfig(driver: String, url: String, user: String, password: String)

  // components have only required dependencies
  val api: URLayer[Has[AppConfig], Has[ApiConfig]]           = ZLayer.fromService(_.api)
  val database: URLayer[Has[AppConfig], Has[DatabaseConfig]] = ZLayer.fromService(_.database)

  object Configuration {
    val configDescription = DeriveConfigDescriptor.descriptor[AppConfig]
    val live              = TypesafeConfig.fromDefaultLoader(configDescription)
  }
}
