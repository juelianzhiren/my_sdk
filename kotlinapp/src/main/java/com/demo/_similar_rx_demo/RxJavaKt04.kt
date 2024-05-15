package k03.rx04

// TODO KT精华 难度极高

fun main() {
    create {
        "Derry"
    }.map {
        length
    }.map {
        "内容的长度是:$this"
    }.map {
        "【$this】"
    }.consumer {
        println("消费:$this")
    }
}

class Helper<T>(var item: T)

fun <T, R> Helper<T>.map(action: T.() -> R) = Helper(action(item))
fun <T> Helper<T>.consumer(action: T.() -> Unit) = action(item)
fun <T> create(action: () -> T) = Helper(action())

// 21:35开始

// Glide Java写的       Coll（KT）