package com.demo

class Helper

fun Helper.button(action: () -> Unit) {

}

fun Helper.text(action: () -> Unit) {

}

// 思路一：一旦看到 {} 这个就是属于 lambda体， 我们就定义lambda规则
// 思路二：{}里面有 button text 我们需要让此lambda持有一个this，此this能够直接拿到 button text
fun layout(action: Helper.() -> Unit) {
    val helper = Helper();
    helper.action();
}

// TODO  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

class IntentFilter // 第三个中转站
{
    class Sub {
        fun action(content: String) {}
        fun category(content: String) {}
        fun action1() {}
        fun action2() {}
        fun action3() {}
    }

    fun intent_filter(lambda: Sub.() -> Unit) {

    }
}

class Component { // 第二个中转站
    fun activity(content: String, lambda: IntentFilter.() -> Unit) : IntentFilter {
        val intentFilter = IntentFilter()
        intentFilter.lambda()
        return intentFilter
    }

    fun service(content: String, lambda: IntentFilter.() -> Unit) : IntentFilter {
        val intentFilter = IntentFilter()
        intentFilter.lambda()
        return intentFilter
    }

    fun receiver(content: String, lambda: IntentFilter.() -> Unit) : IntentFilter {
        val intentFilter = IntentFilter()
        intentFilter.lambda()
        return intentFilter
    }

    fun provider(content: String, lambda: IntentFilter.() -> Unit) : IntentFilter {
        val intentFilter = IntentFilter()
        intentFilter.lambda()
        return intentFilter
    }
}

class Manifest  // 第一个中转站
{
    fun pkg(content: String) {}

    fun permission(content: String) {}

    fun application(action: Component.() -> Unit) : Component {
        val component = Component()

        component.action()

        return component
    }

    // infix 必须是成双成对的
    // infix  "abc".to(123)   "abc" to 123
    // 119-Kotlin语言的infix关键字.mp4
}

// 思路：: Manifest 最终的成果，是要返回解析的字符串
fun manifest(lambda: Manifest.() -> Unit) : Manifest
{
    val manifest = Manifest()

    manifest.lambda() // 不写这句话 和 写这句话的 区别在哪里 ？  就是为了 { 里面的代码执行起来 }

    return manifest
}


fun main() {

    // 目前很火的， 第一胎：View ViewGroup     第二胎UI是谁？Compose

    // 稍微模拟下：JetPacket Compose 手写

    // TODO 入门篇
    // DSL  领域特定语言  == 声明式UI（Compose）
    layout {
        button {

        }

        text {

        }
    }

    // TODO AndroidManifest.xml 上节课没有说完
    manifest {
        pkg("com.derry.kt_base") // 包名
        permission("android.permission.INTERNET") // 权限

        // 四大组件
        application {  // this == 第二个中转站 Component { activity service }
            activity(".MainActivity") { // this == 第三个中转站 IntentFilter
                intent_filter { // this == 第四个中转站 Sub
                    // action1 action2 action3
                    action("android.intent.action.MAIN")
                    category("android.intent.category.LAUNCHER")
                }
            }

            service(".MyService") {
                intent_filter {

                }
            }

            receiver(".MyCustomReceiver") {
                intent_filter {

                }
            }

            provider(".MyCustomReceiver") {
                intent_filter {

                }
            }
        }
    }
}