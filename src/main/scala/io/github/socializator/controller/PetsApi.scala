package io.github.socializator.controller

import zio._
import zio.interop.catz._
import org.http4s._
import io.github.socializator.generated.server.pets.PetsHandler
import io.github.socializator.generated.server.pets.PetsResource
import io.github.socializator.database.PetsRepository
import io.github.socializator.generated.server.pets.CreatePetsResponse
import io.github.socializator.generated.server.pets.ListPetsResponse
import io.github.socializator.generated.server.pets.ShowPetByIdResponse

object PetsApi {

  def routes[R <: Has[PetsRepository.Service]]: HttpRoutes[RIO[R, *]] = {
    type AppTask[A] = RIO[R, A]

    val handler = new PetsHandler[AppTask] {
      def createPets(
          respond: CreatePetsResponse.type
      )(): AppTask[CreatePetsResponse] = {
        PetsRepository.insert("name", Some("tag")).map(_ => respond.Created)
      }
      def listPets(respond: ListPetsResponse.type)(
          limit: Option[Int]
      ): AppTask[ListPetsResponse] = ???
      def showPetById(respond: ShowPetByIdResponse.type)(
          petId: String
      ): AppTask[ShowPetByIdResponse] = ???
    }

    new PetsResource[AppTask]().routes(handler)
  }
}
