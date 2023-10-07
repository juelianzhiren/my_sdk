package com.demo.kotlinapp;

import java.util.ArrayList;
import java.util.List;

public class Demo {
    public static void main(String[] args) {
        Integer[] ints = new Integer[3];
        ints[0] = 0;
        ints[1] = 1;
        ints[2] = 2;
        Number[] numbers = new Number[3];
        numbers = ints;

        for (Number n : numbers) {
            System.out.println(n);
        }

        List<Integer> integerList = new ArrayList<>();
        integerList.add(0);
        integerList.add(1);
        integerList.add(2);
        List<Number> numberList = new ArrayList<>();
//        numberList = integerList;

        List<? extends Number> list = new ArrayList<Number>();
//        list.add(new Integer(1));
    }
}
