package com.demo.delegate.simple1

interface DB {
    fun save()
}

class SqlDB() : DB {
    override fun save() = println("save to sql")
}

class MySqlDB() : DB {
    override fun save() = println("save to MySqlDB")
}

class OracleDB() : DB {
    override fun save() = println("save to Oracle")
}

// 委托含义解释： 将接口的实现 委托给了参数db
class CreateDBAction(db: DB) : DB by db
/*
public final class CreateDBAction implements DB {
   // $FF: synthetic field
   private final DB $$delegate_0;

   public CreateDBAction(@NotNull DB db) {
      Intrinsics.checkParameterIsNotNull(db, "db");
      super();
      this.$$delegate_0 = db;
   }

    @Override
    public void save() {
      this.$$delegate_0.save();
    }
}
 */


fun main() {
    CreateDBAction(SqlDB()).save()
    CreateDBAction(OracleDB()).save()
    CreateDBAction(MySqlDB()).save()
}