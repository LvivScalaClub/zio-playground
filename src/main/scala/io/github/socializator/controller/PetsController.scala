package io.github.socializator.controller

import io.github.socializator.generated.server.pets.PetsHandler
import cats.Applicative
import cats.implicits._
import io.github.socializator.generated.server.pets.CreatePetsResponse
import io.github.socializator.generated.server.pets.ListPetsResponse
import io.github.socializator.generated.server.pets.ShowPetByIdResponse
import io.github.socializator.generated.server.definitions.Pet

class PetsController[F[_]: Applicative]() extends PetsHandler[F] {
  var storage = Map.empty[Int, Pet]

  override def createPets(
      respond: CreatePetsResponse.type
  )(): F[CreatePetsResponse] = {
    (respond.Created: CreatePetsResponse).pure[F]
  }

  override def listPets(
      respond: ListPetsResponse.type
  )(limit: Option[Int]): F[ListPetsResponse] = {
    (respond.Ok(Vector(Pet(1, "Pet", tag = Some("tag"))), None): ListPetsResponse)
      .pure[F]
  }

  override def showPetById(
      respond: ShowPetByIdResponse.type
  )(petId: String): F[ShowPetByIdResponse] = {
    (respond.Ok(Pet(1, "Pet", tag = Some("tag"))): ShowPetByIdResponse).pure[F]
  }

}
