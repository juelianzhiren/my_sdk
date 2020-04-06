package danny.jiang.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class LifeCyclePlugin implements Plugin<Project> {
    private static final String TAG = "noahedu.LifeCyclePlugin";

    @Override
    public void apply(Project project) {
        System.out.println("==LifeCyclePlugin gradle plugin==");
    }
}