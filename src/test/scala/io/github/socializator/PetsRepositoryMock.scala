package io.github.socializator

import io.github.socializator.database.PetsRepository
import io.github.socializator.database.PetsRepository.HasPetsRepository
import io.github.socializator.generated.server.definitions.Pet
import zio._
import zio.test.mock.{Mock, Proxy}

object PetsRepositoryMock extends Mock[HasPetsRepository] {
  object Insert extends Effect[(String, Option[String]), Throwable, Pet]

  val compose: URLayer[Has[Proxy], HasPetsRepository] =
    ZLayer.fromServiceM { proxy =>
      withRuntime.map { rts =>
        new PetsRepository.Service {
          override def insert(name: String, tag: Option[String]): Task[Pet] =
            proxy(Insert, name, tag)
        }
      }
    }
}
