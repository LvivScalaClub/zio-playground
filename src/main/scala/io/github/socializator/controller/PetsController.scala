package io.github.socializator.controller

import io.github.socializator.generated.server.pets.PetsHandler
import cats.Applicative
import cats.implicits._
import io.github.socializator.generated.server.pets.CreatePetsResponse
import io.github.socializator.generated.server.pets.ListPetsResponse
import io.github.socializator.generated.server.pets.ShowPetByIdResponse
import io.github.socializator.generated.server.definitions.Pet
import java.util.concurrent.atomic.AtomicLong

class PetsController[F[_]: Applicative]() extends PetsHandler[F] {
  private val IdSeq = new AtomicLong(0)
  private val storage = scala.collection.mutable.Map.empty[Long, Pet]

  override def createPets(
      respond: CreatePetsResponse.type
  )(): F[CreatePetsResponse] = {
    val id = IdSeq.incrementAndGet()
    storage += id -> Pet(1, s"Pet$id", tag = Some(s"tag$id"))
    (respond.Created: CreatePetsResponse).pure[F]
  }

  override def listPets(
      respond: ListPetsResponse.type
  )(limit: Option[Int]): F[ListPetsResponse] = {
    (respond.Ok(storage.values.toVector, None): ListPetsResponse)
      .pure[F]
  }

  override def showPetById(
      respond: ShowPetByIdResponse.type
  )(petId: String): F[ShowPetByIdResponse] = {
    // never ever do this, refactor later to return 404
    val pet = storage.get(petId.toLong).getOrElse(throw new RuntimeException("NotFound"))
    (respond.Ok(pet): ShowPetByIdResponse).pure[F]
  }

}
