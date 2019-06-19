import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.higherKinds

trait Calcer[F[_]] {
  def getCulc(x: Int, y: Int): F[Int]
}


object HKDemo extends App  {

//  val futureCalcer: Calcer[Future] = (x, y) => Future {x + y}
//  val optionCalcer: Calcer[Option] = (x, y) => Option(x + y)


  def userCalcer[F[_]](implicit calcer: Calcer[F]): F[Int] = calcer.getCulc(1, 2)

  def doItInFutureContext(): Unit = {
    implicit val futureCalcer: Calcer[Future] = (x, y) => Future {x + y}
    println(userCalcer)
  }
  doItInFutureContext()

  def doItInOptionContext(): Unit = {
    implicit val optionCalcer: Calcer[Option] = (x, y) => Option(x + y)
    println(userCalcer)
  }
  doItInOptionContext()
}
