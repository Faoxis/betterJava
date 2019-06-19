import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun getResultFromFirstService(): Int?  {
    println(Thread.currentThread().name)
    return 40
}
fun getResultFromSecondService(number: Int): Int? {
    println(Thread.currentThread().name)
    return number + 2
}

fun main() = runBlocking { // start main coroutine

    val result = async {
        withContext(Dispatchers.Default) { getResultFromFirstService() }?.let { first ->
            withContext(Dispatchers.Default) { getResultFromSecondService(first) }?.let { second ->
                "$first $second"
            }
        }
    }
    println(result.await())

}




