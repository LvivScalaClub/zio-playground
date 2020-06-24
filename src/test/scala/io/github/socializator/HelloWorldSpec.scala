package io.github.socializator

import zio._
import zio.interop.catz._
import zio.console._
import zio.test._
import zio.test.Assertion._
import zio.test.mock.Expectation._
import zio.test.environment._
import io.circe.Decoder
import io.circe.literal._
import org.http4s.circe._
import org.http4s.implicits._
import org.http4s.{Status, _}
import org.http4s._
import io.github.socializator.PetsRepositoryMock.Insert
import io.github.socializator.database.PetsRepository.HasPetsRepository
import io.github.socializator.generated.server.definitions.{Pet, PetPostDTO}
import io.github.socializator.controller.PetsApi

object HelloWorldSpec extends DefaultRunnableSpec {
  type PetsRepositoryTask[A] = RIO[HasPetsRepository, A]

  val app = PetsApi.routes[HasPetsRepository].orNotFound

  def spec =
    suite("HelloWorldSpec")(
      testM("should create new todo items") {
        val req = HTTPSpec
          .request[PetsRepositoryTask](Method.POST, "/v1/pets")
          .withEntity(json"""{"name": "name", "tag": "tag"}""")

        val io = app.run(req)

        assertM(io.map { r =>
          r.status
        })(equalTo(Status.Created))
      }
    ).provideSomeLayer[ZEnv](
      PetsRepositoryMockEnv
// todo
//      InMemoryTodoRepository.layer
    )

  val PetsRepositoryMockEnv: ULayer[HasPetsRepository] =
    (
      Insert(
        equalTo(
          PetPostDTO(
            "name",
            Some("tag")
          )
        ),
        value(
          Pet(
            0,
            "name",
            Some("tag")
          )
        )
      )
    )
}
