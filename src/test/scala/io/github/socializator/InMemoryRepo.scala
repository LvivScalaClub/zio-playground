package io.github.socializator

import zio._
import io.github.socializator.database.PetsRepository
import io.github.socializator.database.PetsRepository.HasPetsRepository
import io.github.socializator.generated.server.definitions.{Pet, PetPostDTO}

final class InMemoryRepo(ref: Ref[Map[Long, Pet]], counter: Ref[Long]) {

  val todoRepository: PetsRepository.Service = new PetsRepository.Service {

    override def insert(body: PetPostDTO): Task[Pet] =
      for {
        newId <- counter.updateAndGet(_ + 1)
        todo = Pet(
          id = newId,
          name = body.name,
          tag = body.tag
        )
        _ <- ref.update(pet => pet + (newId -> todo))
      } yield todo

    override def get(id: Long): Task[Option[Pet]] =
      ref.get.map(_.get(id))
  }
}

object InMemoryTodoRepository {

  val layer: ZLayer[Any, Nothing, HasPetsRepository] =
    ZLayer.fromEffect {
      for {
        ref     <- Ref.make(Map.empty[Long, Pet])
        counter <- Ref.make(0L)
      } yield new InMemoryRepo(ref, counter).todoRepository
    }
}
