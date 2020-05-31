package io.github.socializator

import zio._
import zio.logging.Logging
import zio.logging.slf4j.Slf4jLogger

package object logging {
  object AppLogging {
    val live: ULayer[Logging] =
      Slf4jLogger.make(
        logFormat = (context, line) => line,
        rootLoggerName = None
      )
  }
}
