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
import org.http4s.implicits._
import io.github.socializator.PetsRepositoryMock.{Get, Insert}
import io.github.socializator.database.PetsRepository.HasPetsRepository
import io.github.socializator.generated.server.definitions.{Pet, PetPostDTO}
import io.github.socializator.controller.PetsApi

// val app = TodoService.routes[TodoRepository]("").orNotFound
//
//  override def spec =
//    suite("TodoService")(
//      testM("should create new todo items") {
//        val req = request[TodoTask](Method.POST, "/")
//          .withEntity(json"""{"title": "Test"}""")
//        checkRequest(
//          app.run(req),
//          Status.Created,
//          Some(json"""{
//            "id": 1,
//            "url": "/1",
//            "title": "Test",
//            "completed":false,
//            "order":null
//          }""")
//        )
//      },

object HelloWorldSpec extends DefaultRunnableSpec {
  type PetsRepositoryTask[A] = RIO[HasPetsRepository, A]

//  val app = (PetsApi.routes[HasPetsRepository]).orNotFound
  val app = PetsApi.routes[HasPetsRepository].orNotFound

  def spec =
    suite("HelloWorldSpec")(
      testM("should create new todo items") {
        val req = HTTPSpec
          .request[PetsRepositoryTask](Method.POST, "/v1/pets")
          .withEntity(json"""{"name": "name", "tag": "tag"}""")

        val io = app.run(req)

        assertM(io.map { r =>
          1
        })(equalTo(1))
//        assertM(ZIO.succeed(1))(equalTo(1))
      }
    ).provideSomeLayer[ZEnv](
      PetsRepositoryMockEnv
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
      ) ++
      Get(
        equalTo(1L),
        value(
          Some(
            Pet(
              0,
              "name",
              Some("tag")
            )
          )
        )
      )
    )
}
