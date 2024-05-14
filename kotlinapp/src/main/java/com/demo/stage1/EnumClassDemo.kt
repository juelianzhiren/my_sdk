package com.demo.stage1

// KT想表达：枚举其实也是一个class，为什么，就是为了 枚举可以有更丰富的功能
enum class Week {
    星期一,
    星期二,
    星期三,
    星期四,
    星期五,
    星期六,
    星期日
}

// 四肢信息class，我就是为了方便toString打印
data class LimbsInfo (var limbsInfo: String, var length: Int) {
    fun show() {
        println("${limbsInfo}的长度是:$length")
    }
}

enum class Limbs(private var limbsInfo: LimbsInfo) {
    LEFT_HAND(LimbsInfo("左手", 88)), // 左手
    RIGHT_HAND(LimbsInfo("右手", 88)), // 右手

    LEFT_FOOT(LimbsInfo("左脚", 140)), // 左脚
    RIGHT_FOOT(LimbsInfo("右脚", 140)) // 右脚

    ; // 结束枚举值

    // 1. WEEK 这个时候 再定义单调的 枚举值，就报错了，必须所有枚举值，保持一致的效果
    // 2. 枚举的 主构造的参数 必须和 枚举(的参数) 保持一致

    fun show() = "四肢是:${limbsInfo.limbsInfo}的长度是:${limbsInfo.length}"

    fun updateData(limbsInfo: LimbsInfo) {
        println("更新前的数据是:${this.limbsInfo}")
        this.limbsInfo.limbsInfo = limbsInfo.limbsInfo
        this.limbsInfo.length = limbsInfo.length
        println("更新后的数据是:${this.limbsInfo}")
    }
}


enum class Exam {
    Fraction1, // 分数差
    Fraction2, // 分数及格
    Fraction3, // 分数良好
    Fraction4, // 分数优秀

    ; // 枚举结束

    // 需求 得到优秀的孩子姓名
    var studentName: String? = null
    // 我们用枚举类，要做到此需求，就非常的麻烦了，很难做到而已，不是做不到
    //  需求：引出 密封类
}

class Teacher (private val exam: Exam) {
    fun show() =
        when (exam) {
            Exam.Fraction1 -> "该学生分数很差"
            Exam.Fraction2 -> "该学生分数及格"
            Exam.Fraction3 -> "该学生分数良好"
            Exam.Fraction4 -> "该学生分数优秀"
            // else -> 由于我们的show函数，是使用枚举类类型来做判断处理的，这个就属于 代数数据类型，就不需要写 else 了
            // 因为when表达式非常明确了，就只有 四种类型，不会出现 else 其他，所以不需要写
        }
}


// TODO 95-Kotlin语言的枚举类学习
fun main() {
    println(Week.星期一)
    println(Week.星期四)

    // 枚举的值 等价于 枚举本身
    println(Week.星期二 is Week)




    // TODO 96-Kotlin语言的枚举类定义函数学习
// 显示枚举值

// 一般不会这样用
/*println(Limbs.com.demo.show())
println(Limbs().com.demo.show())*/

// 一般的用法如下：
    println(Limbs.LEFT_HAND.show())
    println(Limbs.RIGHT_HAND.show())
    println(Limbs.LEFT_FOOT.show())
    println(Limbs.RIGHT_FOOT.show())

    println()

// 更新枚举值
    Limbs.RIGHT_HAND.updateData(LimbsInfo("右手2", 99))
    Limbs.LEFT_HAND.updateData(LimbsInfo("左手2", 99))
    Limbs.LEFT_FOOT.updateData(LimbsInfo("左脚2", 199))
    Limbs.RIGHT_FOOT.updateData(LimbsInfo("右脚2", 199))


    println(Teacher(Exam.Fraction1).show())
    println(Teacher(Exam.Fraction3).show())
}