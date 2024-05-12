package com.demo.stage1;

import com.derry.s7.Person;

public class JvmFieldDemo {

    public static void main(String[] args) {
        com.derry.s7.Person person = new Person();
        for (String name : person.names) {
            System.out.println(name);
        }
//        for (String name : person.getNames()) {
//            System.out.println(name);
//        }
    }
}
