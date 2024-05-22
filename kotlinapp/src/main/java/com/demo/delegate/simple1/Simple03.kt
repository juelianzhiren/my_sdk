package com.demo.delegate.simple1

interface Database { // 接口
     fun insert()
}

class MySql : Database { // 实现类
    override fun insert() = println("insert run")
}

// by可以把database接口中的方法，委托给 (database: Database 委托对象 $$delegate_0)
// 委托类，必须是接口才行
// 调用 处理 的细节，交给了 委托，帮我们完成
class CreateDatabase(database: Database) : Database by database  // 委托含义解释：把Database接口的实现 委托给了 参数database
/*
    背后生成的代码：
     public final class CreateDatabase implements Database {
        private Database $$delegate_0; // 委托者 就是 代理

        public CreateDatabase(Database database) {
            $$delegate_0 = database;
        }

        // 重写了接口的方法， 将 insert 委托给了 $$delegate_0.insert();
        @Override
        public void insert() {
            $$delegate_0.insert();
        }
     }
*/

fun main() {
    // 传不同的委托对象进去，它就会有不同的行为。
    CreateDatabase(MySql()).insert()
}