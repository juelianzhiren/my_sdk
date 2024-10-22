package com.derry.ioc_annotation_lib.annation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD) // 作用域我们字段上
@Retention(RetentionPolicy.RUNTIME)
public @interface BindView {
    int value() default -1; // 你的控件 就是 int id值
}