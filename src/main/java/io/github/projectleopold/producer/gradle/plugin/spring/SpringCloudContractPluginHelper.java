/*
 * Copyright 2023 The Project Leopold Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.projectleopold.producer.gradle.plugin.spring;

import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;

public class SpringCloudContractPluginHelper {

    public static final String PLUGIN_ID = "org.springframework.cloud.contract";

    private static TaskProvider<Task> getTask(Project project, String taskName) {
        TaskContainer tasks = project.getTasks();
        TaskProvider<Task> task = tasks.named(taskName);
        if (task.isPresent()) return task;
        throw new IllegalStateException("No task '" + taskName + "'");
    }

    public static TaskProvider<Task> contractTest(Project project) {
        return getTask(project, "contractTest");
    }

    public static TaskProvider<Task> generateClientStubs(Project project) {
        return getTask(project, "generateClientStubs");
    }

}
