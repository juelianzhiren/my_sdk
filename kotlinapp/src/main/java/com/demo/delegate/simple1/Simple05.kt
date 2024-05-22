package com.demo.delegate.simple1

class Simple05 {

    // Version 1.0 版本的样子
    var responseSuccessInfo : String = "Derry isOK"

    // 等在Version 2.0 的时候，需要修改为responseSuccessDatas 更合理，怎么办？
    // 又要兼容 Version 1.0， 同时，也要扩展 Version 2.0，这个适合，可以把Version2.0的委托给Version1.0
    var responseSuccessDatas : String by ::responseSuccessInfo

    // 不放弃第一个版本的值，后续版本就不再关心第一个版本的属性了
}

/*
 背后的代码：
 public final class Simple05 {
    private XXX responseSuccessInfoSingle; // 负责 responseSuccessInfo set get 唯一单例

    // Version 1.0 set get
    private String responseSuccessInfo = "";
    public void setResponseSuccessInfo() {}
    public String getResponseSuccessInfo() {}

    // Version 2.0
    public void setResponseSuccessDatas() { 唯一单例 设置值 调用 --- Version 1.0 set }
    public String getResponseSuccessDatas() { 唯一单例 读取值 调用 --- Version 1.0 get }
 }
*/

fun main() {
    println(Simple05().responseSuccessDatas) // 唯一单例 读取值 调用 --- Version 1.0 get
}