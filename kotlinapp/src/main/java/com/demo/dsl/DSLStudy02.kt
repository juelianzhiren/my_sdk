package com.demo.dsl

import java.io.File

/*fun String.运算符重载+() {
    this == "Derry"
}*/

// 面向对象思维 设计 类的关系（依赖倒转原则） 面向抽象 而 不面向细节
private interface Element { // 元素顶层接口
    /**
     * builder 拼接所有的元素
     * indent  缩进效果
     */
    fun run(builder: StringBuilder, indent: String)
}

// 从简单开始
private class TextElement(val text: String) : Element // 文本的Element，独立的字符串
{
    override fun run(builder: StringBuilder, indent: String) {
        builder.append("$indent$text\n") // 缩进后，显示文本内容
    }
}

// 开始复杂了 <html> <head> ..
private open class Tag(val tagName: String) : Element { // tagName Tag的Element 例如：<html></html>

    val elements = arrayListOf<Element>() // 集合 每一个元素 都是 Element
    val attributes = hashMapOf<String, String>() // Map集合 每一个元素 Key=属性名  Value=属性对应的值

    override fun run(builder: StringBuilder, indent: String) { // 我被 toString 触发
        // <html>  缩进<html 属性>
        builder.append("$indent<$tagName${renderAttributes()}>\n")

        for (element in elements) {
            element.run(builder, indent + "---")
        }

        // </html>  还没有写闭合
        builder.append("$indent</$tagName>\n")
    }

    // 属性拼接： href="http://bbs.xiangxueketang.cn/pins/recommended"
    // null 没有属性
    private fun renderAttributes(): String? {
        val builder = StringBuilder()

        for (key in attributes.keys) {
            // 空格：<a href
            builder.append(" $key=\"${attributes[key]}\"")
        }
        return builder.toString()
    }


    override fun toString(): String { // 触发点1
        val builder = StringBuilder()

        run(builder, "") // 参数一：StringBuilder方便组装html数据，  参数二：indent不要缩进

        return builder.toString()
    }
}

// 解决 +"Derry"  -"AAA"  运算符重载
private open class TagClass(tagName: String) : Tag(tagName = tagName) {

    operator fun String.unaryPlus() { // 运算符重载 +
        elements.add(TextElement(this)) // elements.add
    }

    operator fun String.unaryMinus() { // 运算符重载 -
        elements += TextElement(this)
    }
}

// head中转站
private class Head : TagClass("head") {

    // 如果看到{} 报错，基本上都是，没有给{} 定义lambda规则
    fun title(action: Title.() -> Unit) {
        val newTitle = Title()

        newTitle.action()

        elements.add(newTitle)
    }
}

private class Title() : TagClass("title")
private class H1 : Body("h1") // H2 H3 H4 H5
private class P : Body("p")
private class A : Body("a") {
    var href: String
        get() = attributes["href"]!! // !! 我断言你可以拿到
        set(value) {
            attributes["href"] = value
        }
}

private class B : Body("b")
private class LI : Body("li") // LI中转站
private class UL : Body("ul") {
    // ul 调用 一个或很多 li 标签
    fun li(action: LI.() -> Unit) {
        val newLi = LI()

        newLi.action()

        elements.add(newLi)
    }
}

// ...

// body中转站
private open class Body(tagName: String) : TagClass(tagName = tagName) {

    fun h1(action: H1.() -> Unit) {
        val newH1 = H1()

        newH1.action()

        elements += newH1
    }

    fun p(action: P.() -> Unit) {
        val newP = P()

        newP.action()

        elements += newP
    }

    fun a(href: String, action: A.() -> Unit) {
        val newA = A()

        newA.href = href

        newA.action()

        elements += newA
    }

    fun ul(action: UL.() -> Unit) {
        val newUl = UL()

        newUl.action()

        elements += newUl
    }

    fun b(action: B.() -> Unit) {
        val newB = B()

        newB.action()

        elements += newB
    }
}

// 第一个中转站
private class HTML : TagClass("html") {

    fun head(action: Head.() -> Unit) {
        val newHead = Head()

        newHead.action() // 就是为了执行起来

        elements += newHead //  就是为了后面组装
    }

    fun body(action: Body.() -> Unit) {
        val newBody = Body("body")

        newBody.action() // 就是为了执行起来

        elements += newBody //  就是为了后面组装
    }
}

private fun html(html: HTML.() -> Unit): HTML // 必须返回所有结果 给 File 生成
{
    val htmlObj = HTML()

    htmlObj.html()

    return htmlObj
}

fun main(vararg args: String) {

    // +"Derry"

    val names = listOf("Derry1", "Derry2", "Derry3") // 集合

    val result =
            html { // this == 第一个中转站 { head body 。。 }
                head { // this == head中转站 { title }
                    title { +"使用 Kotlin 进行 HTML 编码" }
                }
                body { // this == body中转站 { h1 p a p }
                    h1 { // this == h1中转站 { 未知 }
                    }
                    p { -"此格式可用作 HTML 的替代标记" }

                    // 具有属性和文本内容的元素
                    a(href = "http://bbs.xiangxueketang.cn/pins/recommended") { -"享学论坛" }

                    // 混合内容
                    p {
                        -"Derry老师来了"
                        b { -"Derry是谁" }
                        -"文本。有关更多信息，请参阅"
                        a(href = "http://www.xiangxueketang.cn/") { -"湖南享学" }
                        -"Derry的项目"
                    }
                    p { -"一些文字" }

                    // 从命令行参数生成的内容
                    p {
                        -"命令行参数是："
                        ul { // this == UL中转站 { li 子标签  }
                            for (name in names)
                                li { -name } // this == LI中转站
                        }
                    }
                }
            }
    println(result)
    val file = File("DDD.html")
    file.writeText(result.toString())

    // 老师能讲一下lamada中转站原理么？
    /*head { // 持有this == 中转站 { aaa  bbb  ccc}

        aaa {}

        bbb {}

        ccc {}

    }*/
}