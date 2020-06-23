package io.github.socializator

import io.github.socializator.database.PetsRepository
import io.github.socializator.database.PetsRepository.HasPetsRepository
import io.github.socializator.generated.server.definitions.{Pet, PetPostDTO}
import zio._
import zio.test.mock.{Mock, Proxy}

object PetsRepositoryMock extends Mock[HasPetsRepository] {
  object Insert extends Effect[PetPostDTO, Throwable, Pet]
  object Get extends Effect[Long, Throwable, Option[Pet]]

  val compose: URLayer[Has[Proxy], HasPetsRepository] =
    ZLayer.fromServiceM { proxy =>
      withRuntime.map { rts =>
        new PetsRepository.Service {
//          def insert(body: PetPostDTO): Task[Pet]
          override def insert(body: PetPostDTO): Task[Pet] =
            proxy(Insert, body)

          override def get(id: Long): Task[Option[Pet]] =
            proxy(Get, id)
        }
      }
    }
}
