/*
package com.demo.dsl

import java.io.File

interface Node {
    fun create(): String
}

// html的中转站
class BlockNode(val name: String) : Node {

    val children = ArrayList<Node>() // 节点集合： html head body
    private val properties =
        HashMap<String, Any>() // 属性集合： style='color: white; font-family: Microsoft YaHei'

    override fun create(): String {
        // return """<$name ${properties.map {"${it.key}='${it.value}'" }.joinToString(" ")}>${children.joinToString(""){ it.create() }}</$name>"""
        return """<$name ${
            properties.map { "${it.key}='${it.value}'" }.joinToString(" ")
        }>${children.joinToString("") { it.create() }}</$name>"""
    }

    operator fun String.invoke(action: BlockNode.() -> Unit) {
        // this == "meta"

        // this == String本身

        val stringNode = BlockNode(this)

        stringNode.action()

        this@BlockNode.children += stringNode
    }

    operator fun String.invoke(value: Any) {
        // 保存到属性里面去
        this@BlockNode.properties[this] = value
    }

    operator fun String.unaryPlus() {
        val stringNode = StringNode("$this&nbsp;&nbsp;")

        this@BlockNode.children += stringNode
    }
}

class StringNode(private val value: String) : Node {
    override fun create() = value
}

fun html(lambdaAction: BlockNode.() -> Unit): BlockNode // 要去生成file，所以必须返回最终成果
{
    val html = BlockNode("html")

    html.lambdaAction()

    return html
}

fun BlockNode.head(lambdaAction: BlockNode.() -> Unit) {
    val head = BlockNode("head")

    head.lambdaAction()

    // 把tag加入节点
    children += head
}

fun BlockNode.body(lambdaAction: BlockNode.() -> Unit) {
    val body = BlockNode("body")

    body.lambdaAction()

    // 把tag加入节点
    children += body
}

fun main() {

    // "Derry"

    val htmlContent = html { // this持有中转站BlockNode
        head { // this持有中转站BlockNode

            // String.invoke(Any)
            "meta" { "charset"("UTF-8") }
        }
        body {
            "div" {
                "style"(
                    """
                    width: 666px; 
                    height: 666px; 
                    line-height: 600px; 
                    background-color: #F00;
                    text-align: center
                    """.trimIndent()
                )
                "span" {
                    "style"(
                        """
                        color: white;
                        font-family: Microsoft YaHei
                        """.trimIndent()
                    )
                    +"你好 HTML DSL！！"
                    +"我就是我，不一样的烟火"
                    +"像我这样牛逼的人"
                    +"世界上还有几人"
                }
            }
        }
    }.create() // 用户调用create函数，我就组装

    File("ddd.html").writeText(htmlContent)
}*/
