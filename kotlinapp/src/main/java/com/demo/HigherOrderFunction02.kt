// TODO lambda上节课 的考试，此考试意味着，考试掌握的话，你后面会轻松
fun main() {
    // 考试：函数返回一个函数
    var fun4 = fun(n1aaa: Int, n2aaa: Int) :  // fun4函数本身
                (Int, Int) -> String // fun4函数的函数 以返回值返回了这个函数
            = {n1, n2 -> "相加的结果是:${n1 + n2}  $n1aaa"} //  // fun4函数的函数 以返回值返回了这个函数 的 实现
    println(fun4(100, 100)(1000, 1000))

    // 考试：你掌握了，你的lambda基本上每一对手了
    var k01 : (String) -> (String) ->(Boolean) -> (Int) -> (String) -> Int =
    {
        {
            {
                {
                    {
                        99
                    }
                }
            }
        }

    }
    println(k01("AAA")("BBB")(true)(800)("Derry"))
}