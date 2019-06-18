import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

suspend fun getResultFromFirstService(): Int? = 40
suspend fun getResultFromSecondService(): Int? = 2


fun main() = runBlocking { // start main coroutine
    val channel = Channel<Int?>()

    launch {
        getResultFromFirstService()

    }
    launch {
        channel.receive()?.let { first ->
            getResultFromSecondService()?.let { second ->
                first + second
            }
        }.let {
            println(it)
        }
    }

    delay(2000)
}



