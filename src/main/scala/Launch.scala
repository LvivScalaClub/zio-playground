import zio.ZIO
import zio.console._

object Launch extends zio.App {

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, Int] = {
    for {
      _ <- putStrLn("Hello World")
    } yield 0
  }
}
