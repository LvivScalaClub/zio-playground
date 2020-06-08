package io.github.socializator.database

import doobie._
import doobie.implicits._
import cats._
import cats.effect._
import cats.implicits._
import io.github.socializator.generated.server.definitions.Pet
import zio._
import zio.interop.catz._
import doobie.util.transactor

final class PetsRepository(transactor: Transactor[Task]) {
  val service = new PetsRepository.Service {
    def insert(name: String, tag: Option[String]): Task[Pet] = {
      PetsRepository.SQL
        .insert(name, tag)
        .transact(transactor)
    }
  }
}

object PetsRepository extends Serializable {
  def insert(name: String, tag: Option[String]): RIO[Has[PetsRepository.Service], Pet] =
    ZIO.accessM(_.get.insert(name, tag))

  trait Service extends Serializable {
    def insert(name: String, tag: Option[String]): Task[Pet]
  }

  object SQL {
    def insert(name: String, tag: Option[String]): ConnectionIO[Pet] =
      sql"""INSERT INTO pets (name, tag) VALUES (${name}, ${tag})""".update
        .withUniqueGeneratedKeys[Pet]("id", "name", "tag")
  }

  val live: URLayer[Has[Transactor[Task]], Has[PetsRepository.Service]] = ZLayer.fromService {
    transactor: Transactor[Task] =>
      new PetsRepository(transactor).service
  }
}
