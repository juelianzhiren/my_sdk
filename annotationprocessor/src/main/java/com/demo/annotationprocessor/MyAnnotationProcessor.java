package com.demo.annotationprocessor;

import com.demo.annotation.MyAnnotation;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@SupportedAnnotationTypes("com.demo.annotation.MyAnnotation")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class MyAnnotationProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
            for (Element element : annotatedElements) {
                // 这里处理你的注解信息，例如打印注解的值：
                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, element + " 使用了 @MyAnnotation 注解，值为：" + getAnnotationValue(element, MyAnnotation.class));
            }
        }
        return true; // 表示注解处理器已经处理过这些元素，不需要再次处理。
    }

    private String getAnnotationValue(Element element, Class<? extends MyAnnotation> annotationType) {
        try {
            return element.getAnnotation(annotationType).value().toString();
        } catch (Exception e) {
            return "无法获取注解值";
        }
    }
}