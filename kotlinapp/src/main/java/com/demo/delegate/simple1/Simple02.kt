package com.demo.delegate.simple1

interface IUSB {
    fun insert()

    /*fun insert2()
    fun insert3()
    fun insert4()
    fun insert5()
    // 20*/
}

class Mouse : IUSB {
    override fun insert() = println("鼠标插入了USB")
}

class KeyBard : IUSB {
    override fun insert() = println("键盘插入了USB")
}

// 委托含义解释：把接口的实现，委托给了参数iusb
class CreateUSB(iusb: IUSB) : IUSB by iusb
/*
    背后生成的代码：
    public final class CreateUSB implements IUSB {

        private IUSB $$delegate_0;

        public CreateDB(IUSB iusb) {
            this.$$delegate_0 = iusb;
        }

        @Override
        public void insert() {
            this.$$delegate_0.save();
        }
    }
 */

// 不使用委托(全部都要自己完成实现)      如果使用委托 实现细节交给委托完成，我们不管了
/*class CreateUSB2(iusb: IUSB) : IUSB {

    val iusb_ = iusb

    override fun insert() {
        iusb_.insert()
    }

    override fun insert2() {
        TODO("Not yet implemented")
    }

    override fun insert3() {
        TODO("Not yet implemented")
    }

    override fun insert4() {
        TODO("Not yet implemented")
    }

    override fun insert5() {
        TODO("Not yet implemented")
    }
}*/

fun main() {
    CreateUSB(Mouse()).insert() // new CreateUSB(new Mouse()).insert();
    CreateUSB(KeyBard()).insert() // new CreateUSB(new KeyBard()).insert();
}