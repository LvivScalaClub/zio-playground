package io.github.socializator.database

import doobie._
import doobie.implicits._
import doobie.quill.DoobieContext.Postgres
import io.github.socializator.generated.server.definitions.{Pet, PetPostDTO}
import zio._
import zio.interop.catz._
import io.getquill.{idiom => _, _}

final class PetsRepository(transactor: Transactor[Task], doobieContext: Postgres[Literal]) {
  val service = new PetsRepository.Service {
    def insert(body: PetPostDTO): Task[Pet] = {
      SQL
        .insert(body)
        .transact(transactor)
    }
    def get(id: Long): Task[Option[Pet]] = {
      SQL.get(id).transact(transactor)
    }

  }

  object SQL {

    import doobieContext._

    def insert(body: PetPostDTO): ConnectionIO[Pet] = {
      val petToInsert = Pet(0, body.name, body.tag)
      val q = quote {
        query[Pet]
          .insert(lift(petToInsert))
          .returningGenerated(_.id)
      }

      run(q).map(id => petToInsert.copy(id = id))
    }

    def get(id: Long): doobie.ConnectionIO[Option[Pet]] = {
      val q = quote {
        query[Pet]
          .filter(_.id == lift(id))
      }
      run(q).map(_.headOption)
    }
  }

}

object PetsRepository extends Serializable {
  def insert(body: PetPostDTO): RIO[Has[PetsRepository.Service], Pet] =
    ZIO.accessM(_.get.insert(body))

  def get(id: Long): RIO[Has[PetsRepository.Service], Option[Pet]] =
    ZIO.accessM(_.get.get(id))

  trait Service extends Serializable {
    def insert(body: PetPostDTO): Task[Pet]

    def get(id: Long): Task[Option[Pet]]
  }

  val live: URLayer[Has[Transactor[Task]] with Has[Postgres[Literal]], Has[PetsRepository.Service]] = {
    val service = for {
      transactor      <- ZIO.access[Has[Transactor[Task]]](_.get)
      postgresContext <- ZIO.access[Has[Postgres[Literal]]](_.get)
    } yield new PetsRepository(transactor, postgresContext).service
    service.toLayer
  }
}
