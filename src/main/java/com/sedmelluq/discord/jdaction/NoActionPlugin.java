package com.sedmelluq.discord.jdaction;

import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.file.FileCollection;
import org.gradle.api.internal.ConventionMapping;
import org.gradle.api.plugins.quality.CodeQualityExtension;
import org.gradle.api.plugins.quality.internal.AbstractCodeQualityPlugin;
import org.gradle.api.tasks.SourceSet;

import java.util.concurrent.Callable;

public class NoActionPlugin extends AbstractCodeQualityPlugin<NoActionVerificationTask> {
  @Override
  protected String getToolName() {
    return "jdaction";
  }

  @Override
  protected Class<NoActionVerificationTask> getTaskType() {
    return NoActionVerificationTask.class;
  }

  @Override
  protected CodeQualityExtension createExtension() {
    extension = project.getExtensions().create("jdaction", Extension.class, project);
    return extension;
  }

  @Override
  protected void configureForSourceSet(SourceSet sourceSet, NoActionVerificationTask task) {
    task.setSource(sourceSet.getAllJava());

    ConventionMapping taskMapping = task.getConventionMapping();
    taskMapping.map("classes", new Callable<FileCollection>() {
      @Override
      public FileCollection call() {
        return project.fileTree(sourceSet.getOutput().getClassesDir()).builtBy(sourceSet.getOutput());
      }
    });

    for (Task classesTask : project.getTasksByName(sourceSet.getClassesTaskName(), false)) {
      classesTask.finalizedBy(task.getPath());
    }
  }

  public static class Extension extends CodeQualityExtension {
    @SuppressWarnings("unused")
    public Extension(Project project) {

    }
  }
}
