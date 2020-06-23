package io.github.socializator

import io.github.socializator.configuration.DatabaseConfig
import cats.effect._
import cats.implicits._
import doobie._
import doobie.implicits._
import doobie.hikari._
import zio._
import zio.blocking.Blocking
import zio.interop.catz._
import scala.concurrent.ExecutionContext
import org.flywaydb.core.Flyway

package object database {
  def migrateDatabaseSchema(cfg: DatabaseConfig): Task[Unit] =
    Task {
      Flyway
        .configure()
        .dataSource(cfg.url, cfg.user, cfg.password)
        .load()
        .migrate()
    }.unit

  object AppTransactor {
    def mkHikariTransactor(
        cfg: DatabaseConfig
    ): ZManaged[Has[Blocking.Service], Throwable, Transactor[Task]] = {
      ZIO.runtime[Blocking].toManaged_.flatMap { implicit rt =>
        for {
          transactEC <- Managed.succeed(
            rt.environment
              .get[Blocking.Service]
              .blockingExecutor
              .asEC
          )
          connectEC = rt.platform.executor.asEC
          transactor <-
            HikariTransactor
              .newHikariTransactor[Task](
                cfg.driver,
                cfg.url,
                cfg.user,
                cfg.password,
                connectEC,
                Blocker.liftExecutionContext(transactEC)
              )
              .toManaged
        } yield transactor
      }
    }

    val live: ZLayer[Has[Blocking.Service] with Has[DatabaseConfig], Throwable, Has[Transactor[Task]]] =
      ZLayer.fromManaged {
        for {
          cfg        <- ZIO.access[Has[DatabaseConfig]](_.get).toManaged_
          transactor <- mkHikariTransactor(cfg)
        } yield transactor
      }

    val test: ZLayer[Has[Blocking.Service] with Has[DatabaseConfig], Throwable, Has[Transactor[Task]]] =
      ZLayer.fromManaged {
        for {
          cfg        <- ZIO.access[Has[DatabaseConfig]](_.get).toManaged_
          transactor <- mkHikariTransactor(cfg)
        } yield transactor
      }
  }
}
