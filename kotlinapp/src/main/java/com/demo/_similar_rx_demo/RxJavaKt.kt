package k03.rx01

// TODO KT精华 难度极高
fun main() {
    /*setText {
        buttom {
            setColor {

            }
        }
    }

    html {
        head {

        }
        body {

        }
    }*/

    create {
        "Derry"
        "Derry"
        "Derry"
        "Derry"
        "Derry1"
        654675
        4655.554
    }.map {
        123
    }.map {
        true
    }.map {
        3333.345
    }.consumer {
        println("消费:$it")
    }

    create {
        "Derry"
    }.map {
        it.length
    }.map {
        "内容的长度是:$it"
    }.map {
        "【$it】"
    }.consumer {
        println("消费:$it")
    }
}

// 中转站目的：两个  保存最新的item，  给map输入用  给map输出保存到item

// 中转站 保存你要流向下去的数据
class Helper<T>(private var item: T) { // private var item : T  这个item是一直在更新的

    // map可以链式调用
    fun <R> map(action: (T) -> R): Helper<R> {
        // RxJava变化操作， T 变成 R
        val newItem: R = action(item)
        return Helper(newItem)
    }

    // 思路：消费，只需要 上一个操作符的返回类型就可以了，我不需要返回，所以没有R
    fun consumer(action: (T) -> Unit) = action(item)
}

// 思路：输入你不需要考虑，因为是最后一行最为流向的数据
// 思路：中转站就是为了转化(保存)我们的 create 和 map 等等的数据
fun <T> create(action: () -> T): Helper<T> {
    val r: T = action()
    return Helper(r)
}

// 中转站 就是要维护这个效果： 上一个操作符的返回值 是下一个操作符的参数
// 21:35开始