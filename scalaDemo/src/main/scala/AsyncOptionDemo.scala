import cats.data.OptionT

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object AsyncOptionDemo extends App {

  calcResultOfTwoServices(_ => Option(40), _ => Option(2)).foreach(println)
  Thread.sleep(100)
  calcResultOfTwoServices(_ => Option.empty[Int], _ => Option(2)).foreach(println)
  Thread.sleep(100)
  calcResultOfTwoServices(_ => Option(40), _ => Option.empty[Int]).foreach(println)

  Thread.sleep(1000)

//// version 1
//def calcResultOfTwoServices(getResultFromFirstService: Unit => Option[Int],
//                            getResultFromSecondService: Unit => Option[Int]) =
//  Future {
//    getResultFromFirstService()
//  }.flatMap { firsResultOption =>
//    Future { firsResultOption.flatMap(x =>
//      getResultFromSecondService().map(y =>
//        x + y
//      )
//    )}
//  }

//  // version 2
//  def calcResultOfTwoServices(getResultFromFirstService: Unit => Option[Int],
//                              getResultFromSecondService: Unit => Option[Int]) =
//    Future {
//      getResultFromFirstService()
//    }.flatMap { firstResultOption =>
//      Future {
//        for {
//          a <- firstResultOption
//          b <- getResultFromSecondService()
//        } yield a + b
//      }
//    }

  // version 3 (with cats)
  import cats.instances.future._

  def calcResultOfTwoServices(getResultFromFirstService: Unit => Option[Int],
                              getResultFromSecondService: Unit => Option[Int]): Future[Option[Int]] =
    (for {
      first <- OptionT(Future { getResultFromFirstService() })
      second <- OptionT(Future { getResultFromSecondService() })
    } yield first + second).value
}

