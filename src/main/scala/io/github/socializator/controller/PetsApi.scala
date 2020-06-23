package io.github.socializator.controller

import zio._
import zio.interop.catz._
import org.http4s._
import io.github.socializator.generated.server.pets.PetsHandler
import io.github.socializator.generated.server.pets.PetsResource
import io.github.socializator.database.PetsRepository
import io.github.socializator.error.{InternalAppError, PetNotFoundError}
import io.github.socializator.generated.server.definitions.PetPostDTO
import io.github.socializator.generated.server.pets.CreatePetsResponse
import io.github.socializator.generated.server.pets.ListPetsResponse
import io.github.socializator.generated.server.pets.ShowPetByIdResponse

object PetsApi {

  def routes[R <: Has[PetsRepository.Service]]: HttpRoutes[RIO[R, *]] = {
    type AppTask[A] = RIO[R, A]

    val handler = new PetsHandler[AppTask] {
      def createPets(respond: CreatePetsResponse.type)(body: PetPostDTO): AppTask[CreatePetsResponse] = {
        PetsRepository.insert(body).map(CreatePetsResponse.Created)
      }

      def listPets(respond: ListPetsResponse.type)(
          limit: Option[Int]
      ): AppTask[ListPetsResponse] = {
        RIO
          .fail(InternalAppError("Some test error")) //Check that error handler handle this error too
          .map(_ => respond.Ok(Vector.empty, None))
      }

      def showPetById(respond: ShowPetByIdResponse.type)(petId: BigInt): AppTask[ShowPetByIdResponse] = {
        PetsRepository.get(petId.longValue).map {
          case Some(value) => ShowPetByIdResponse.Ok(value)
          case None        => ShowPetByIdResponse.BadRequest(PetNotFoundError(s"Pet was not found for id: $petId").toApiError)
        }
      }
    }

    new PetsResource[AppTask]().routes(handler)
  }
}
