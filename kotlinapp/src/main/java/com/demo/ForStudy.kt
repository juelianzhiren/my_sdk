package k03

fun main() {
    // TODO 官方的：
    listOf("AAA", "BBB", "CCC")
        .forEach {
            println(it)
        }

    println()

    // TODO 我们自己写的
    listOf("AAA", "BBB", "CCC")
        .mForEach {
            println(it)
        }

    println()

    listOf(111, 222, 333)
        .mForEach2 {
            println(it)
        }

    println()

    listOf(111, 222, 333)
        .mForEach3 {
            println(this)
        }

    println()

    listOf(555, 666, 777)
        .mForEach4 {
            println(this)
        }

    // aaa()()
}

// Set List Map .... 父类  Iterable
private inline fun<T> Iterable<T>.mForEach(lambda : (T) -> Unit) {
    for (item in this) lambda(item)
}

// run 就是执行你的 逻辑
private inline fun<T> Iterable<T>.mForEach2(lambda : (T) -> Unit) = run {  for (item in this) lambda(item) }

private inline fun<T> Iterable<T>.mForEach3(lambda : T.() -> Unit) = run {  for (item in this) lambda(item) }

private inline fun<T> Iterable<T>.mForEach4(lambda : T.() -> Unit) = r {  for (item in this) lambda(item) }

private inline fun<T> Iterable<T>.mForEach5(lambda : T.() -> Unit) =
    r {  for (item in this) lambda(item) }

private inline fun <R> r(lambda: () -> R) = lambda()

// 你对xxx扩展， this==xxx本身