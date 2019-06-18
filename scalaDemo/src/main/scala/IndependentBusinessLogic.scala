import language.higherKinds
import cats.implicits._
import cats.{Id, Monad}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import language.higherKinds

trait CatClinicClient[F[_]] {
  def getHungryCat: F[Int]
  def getFreeMember: F[Int]
  def feedCatByFreeMember(catId: Int, memberId: Int): F[Unit]
}

object CatClinicClient {
  def apply[F[_]](implicit catClinicClient: CatClinicClient[F]): CatClinicClient[F] = catClinicClient
}

class RealCatClinicClient extends CatClinicClient[Future] {
  override def getHungryCat: Future[Int] = Future {
    Thread.sleep(1000) // doing some calls to db (waiting 1 second)
    40
  }
  override def getFreeMember: Future[Int] = Future {
    Thread.sleep(1000) // doing some calls to db (waiting 1 second)
    2
  }
  override def feedCatByFreeMember(catId: Int, memberId: Int): Future[Unit] = Future {
    Thread.sleep(1000) // happy cat (waiting 1 second)
    println("so testy!") // Don't do like that. It is just for debug
  }
}

class MockCatClinicClient extends CatClinicClient[Id] {
  override def getHungryCat: Id[Int] = 40
  override def getFreeMember: Id[Int] = 2
  override def feedCatByFreeMember(catId: Int, memberId: Int): Id[Unit] = {
    println("so testy!") // Don't do like that. It is just for debug
  }
}

object IndependentBusinessLogic extends App {
  implicit val realDbImplicit: CatClinicClient[Future] = new RealCatClinicClient()
  implicit val mockDbImplicit: CatClinicClient[Id] = new MockCatClinicClient()

  def makeCatHappy[F[_]: Monad: CatClinicClient](): F[Unit] =
    for {
      catId <- CatClinicClient[F].getHungryCat
      memberId <- CatClinicClient[F].getFreeMember
      _ <- CatClinicClient[F].feedCatByFreeMember(catId, memberId)
    } yield ()
--
  makeCatHappy[Id]()
  Thread.sleep(4000)
}
