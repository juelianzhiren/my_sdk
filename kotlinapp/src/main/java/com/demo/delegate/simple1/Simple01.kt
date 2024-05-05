package com.demo.delegate.simple1

interface DB {
    fun save()
}

class SqlDB() : DB {
    override fun save() {
        println("save to sqlDb");
    }
}

class MySqlDB() : DB {
    override fun save() {
        println("save to MysqlDb");
    }
}

class OracleDB() : DB {
    override fun save() {
        println("save to Oracle");
    }
}

class CreateDBAction(db : DB) : DB by db

fun main() {
    CreateDBAction(SqlDB()).save()
    CreateDBAction(MySqlDB()).save()
    CreateDBAction(OracleDB()).save()
}