data class Dot (val x: Int, val y: Int)

// неявно получаем ссылку на объект
fun Dot.convertToJson(): String =
        "{\"x\"=$x, \"y\"=$y}"

fun Dot.convertToXml(): String =
        """<dot>
            <x>$x</x>
            <y>$y</y>
        </dot>"""


fun main() {
    val dot = Dot(1, 2)
    println("-------- JSON -----------")
    println(dot.convertToJson())
    println("-------- XML  -----------")
    println(dot.convertToXml())
}

