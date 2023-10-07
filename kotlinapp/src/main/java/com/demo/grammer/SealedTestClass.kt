package com.demo.grammer

sealed class Expr {}

data class Const(val number: Double) : Expr()

data class Sum(val e1: Expr, val e2: Expr) : Expr()

object NotANumber : Expr()

fun eval(expr: Expr): Double = when (expr) {
    is Const -> {
        println(expr.number)
        expr.number
    }
    is Sum -> eval(expr.e1) + eval(expr.e2)
    NotANumber -> Double.NaN
    // 不再需要 `else` 子句，因为我们已经覆盖了所有的情况
}

fun main() {
    eval(Sum(Const(4.toDouble()), Const(5.toDouble())))
    eval(NotANumber)
}