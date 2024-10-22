package com.derry.ioc_annotation_lib.annation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // 作用域在方法之上
@Retention(RetentionPolicy.RUNTIME) // 运行时期
public @interface Click {
    int value() default -1; // 你的控件 就是 int id值
}