package k03.rx03

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

class Helper<T>(private var item: T) {
    fun <R> map(action: T.() -> R) = Helper(action(item))
    fun consumer(action: T.() -> Unit) = action(item)
}

fun <T> create(action: () -> T) = Helper(action())