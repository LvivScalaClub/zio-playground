package io.github.socializator.error

import org.http4s.Status
import io.github.socializator.generated.server.definitions.ApiError

trait AppError extends Throwable {

  def status: Status

  def message: String

  def toApiError: ApiError = ApiError(status.code, message)
}

case class InternalAppError(message: String) extends AppError {
  override def status: Status = Status.InternalServerError
}

case class PetNotFoundError(message: String) extends AppError {
  override def status: Status = Status.NotFound
}
