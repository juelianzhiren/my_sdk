package com.example.plugin

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class ASMPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println("========== this is a log just from PluginDemo ==========")
        BaseExtension android = project.getExtensions().getByType(BaseExtension.class);

        println("========== registerTransform before ==========")
        // android 插件 能够获得所有的class
        // 同时他提供一个接口，能够让我们也获得所有class
        android.registerTransform(new ASMTransform());
        println("========== registerTransform after ==========")
    }
}