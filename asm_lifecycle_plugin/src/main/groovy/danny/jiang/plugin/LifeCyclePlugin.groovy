package danny.jiang.plugin;

import com.android.build.gradle.AppExtension;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class LifeCyclePlugin<T> implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        System.out.println("==LifeCyclePlugin gradle plugin==");

        def android = project.extenclssions.getByType(AppExtension);
        println "--------registering AutoTrackTransform-------";
        LifeCycleTransform transform = new LifeCycleTransform();
        android.registerTransform(transform);
    }
}