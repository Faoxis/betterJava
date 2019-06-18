
object DotDemo extends App {

  // val is default
  case class Dot(x: Int, y: Int)

  implicit class DotConverters(dot: Dot) {
    def convertToJson(): String =
      s"""{"x"=${dot.x}, "y"=${dot.y}}"""
    def convertToXml(): String =
      s"""<dot>
            <x>${dot.x}</x>
            <y>${dot.y}</y>
      </dot>"""
  }

  val dot = Dot(1, 2)
  println("-------- JSON -----------")
  println(dot.convertToJson())
  println("-------- XML  -----------")
  println(dot.convertToXml())
}


