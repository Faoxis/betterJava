import cats.effect._
import cats.syntax.functor._
import fs2.{Stream, io, text}
import java.nio.file.Paths
import java.util.concurrent.Executors

import scala.concurrent.{ExecutionContext, ExecutionContextExecutorService}

object ReadFilesDemo extends IOApp {

  implicit val blockingExecutionContext: ExecutionContextExecutorService =
    ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(2))

  def readFile(filename: String)
              (implicit blockingExecutionContext: ExecutionContextExecutorService): Stream[IO, String] =
    io
      .file
      .readAll[IO](Paths.get(filename), blockingExecutionContext, 4096)
      .through(text.utf8Decode)
      .through(text.lines)

  def findUniqueLines(filename1: String, filename2: String, filename3: String): Stream[IO, String] =
    for {
      lineFromFirstFile <- readFile(filename1)
      lineFromSecondFile <- readFile(filename2).filter(_.equals(lineFromFirstFile))
      result <- readFile(filename3).filter(_.equals(lineFromSecondFile))
    } yield result

  def program(glueFiles: Stream[IO, String])
             (implicit blockingExecutionContext: ExecutionContextExecutorService): IO[Unit] =
      glueFiles
      .evalMap(x => IO(println(x)))
      .compile
      .drain
      .guarantee(IO(blockingExecutionContext.shutdown()))

  override def run(args: List[String]): IO[ExitCode] =
    program(findUniqueLines("first.txt", "second.txt", "third.txt")).as(ExitCode.Success)
}
