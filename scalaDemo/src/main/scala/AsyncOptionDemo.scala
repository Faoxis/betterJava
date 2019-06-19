import cats.data.OptionT

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object AsyncOptionDemo extends App {

  calcResultOfTwoServices(_ => Option(40), number => Option(number + 2)).foreach(println)
  Thread.sleep(100)
  calcResultOfTwoServices(_ => Option.empty[Int], _ => Option(2)).foreach(println)
  Thread.sleep(100)
  calcResultOfTwoServices(_ => Option(40), _ => Option.empty[Int]).foreach(println)

  Thread.sleep(1000)

//// version 1
//def calcResultOfTwoServices(getResultFromFirstService: Unit => Option[Int],
//                            getResultFromSecondService: Int => Option[Int]) =
//  Future {
//    getResultFromFirstService()
//  }.flatMap { firsResultOption =>
//    Future { firsResultOption.flatMap(first =>
//      getResultFromSecondService(first).map(second =>
//        s"$first $second"
//      )
//    )}
//  }

//  // version 2
//  def calcResultOfTwoServices(getResultFromFirstService: Unit => Option[Int],
//                              getResultFromSecondService: Int => Option[Int]) =
//    Future {
//      getResultFromFirstService()
//    }.flatMap { firstResultOption =>
//      Future {
//        for {
//          first <- firstResultOption
//          second <- getResultFromSecondService(first)
//        } yield s"$first $second"
//      }
//    }

  // version 3 (with cats)
  import cats.instances.future._

  def calcResultOfTwoServices(getResultFromFirstService: Unit => Option[Int],
                              getResultFromSecondService: Int => Option[Int]): Future[Option[String]] =
    (for {
      first <- OptionT(Future { getResultFromFirstService() })
      second <- OptionT(Future { getResultFromSecondService(first) })
    } yield s"$first $second").value
}

