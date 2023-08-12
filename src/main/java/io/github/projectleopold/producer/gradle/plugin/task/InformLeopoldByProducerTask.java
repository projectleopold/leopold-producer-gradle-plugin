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

package io.github.projectleopold.producer.gradle.plugin.task;

import io.github.projectleopold.producer.gradle.plugin.explorer.mapping.ContractMapping;
import io.github.projectleopold.producer.gradle.plugin.explorer.mapping.ContractMappingExplorer;
import io.github.projectleopold.producer.gradle.plugin.explorer.test.ContractTestExplorer;
import io.github.projectleopold.producer.gradle.plugin.explorer.test.ContractTestResult;
import io.github.projectleopold.producer.gradle.plugin.spring.SpringCloudContractPluginHelper;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.file.Directory;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.PathSensitive;
import org.gradle.api.tasks.PathSensitivity;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskOutputs;
import org.gradle.api.tasks.TaskProvider;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Path;

public class InformLeopoldByProducerTask extends DefaultTask {

    public static final String TASK_NAME = "informLeopoldByProducer";

    private static final Logger LOGGER = Logging.getLogger(InformLeopoldByProducerTask.class);

    private final DirectoryProperty mappingDirectory;
    private final DirectoryProperty testResultDirectory;

    @Inject
    public InformLeopoldByProducerTask(ObjectFactory objects) {
        this.mappingDirectory = objects.directoryProperty();
        this.testResultDirectory = objects.directoryProperty();
        setGroup(LeopoldProducerTasks.GROUP);
        setDescription("Inform leopold by producer.");
    }

    public static TaskProvider<InformLeopoldByProducerTask> register(Project project) {
        TaskContainer tasks = project.getTasks();
        return tasks.register(TASK_NAME, InformLeopoldByProducerTask.class, task -> {
            // generateClientStubs
            ProjectLayout layouts = project.getLayout();
            TaskProvider<Task> generateClientStubs = SpringCloudContractPluginHelper.generateClientStubs(project);
            Provider<File> mapping = generateClientStubs
                    .map(Task::getOutputs)
                    .map(TaskOutputs::getFiles)
                    .map(FileCollection::getSingleFile);
            //TODO: lazy?
            task.getMappingDirectory().convention(layouts.dir(mapping));
            task.dependsOn(generateClientStubs);
            // contractTest
            TaskProvider<Task> contractTest = SpringCloudContractPluginHelper.contractTest(project);
            Provider<Directory> testResult = layouts
                    .getBuildDirectory()
                    //TODO: get path from contractTest (for different test-frameworks)
                    .dir("test-results/contractTest");
            //TODO: lazy?
            task.getTestResultDirectory().convention(testResult);
            task.dependsOn(contractTest);
            LOGGER.debug("Registered task: '{}'", TASK_NAME);
        });
    }

    @TaskAction
    public void informLeopoldByProducer() {
        ContractMappingExplorer mappingExplorer = new ContractMappingExplorer();
        Path mappingDir = mappingDirectory
                .map(Directory::getAsFile)
                .map(File::toPath)
                .get();
        ContractMapping mapping = mappingExplorer.explore(mappingDir);
        LOGGER.lifecycle("Mapping: {}", mapping);
        ContractTestExplorer testExplorer = new ContractTestExplorer(mapping);
        Path testResultDir = testResultDirectory
                .map(Directory::getAsFile)
                .map(File::toPath)
                .get();
        ContractTestResult testResult = testExplorer.explore(testResultDir);
        LOGGER.lifecycle("TestResult: {}", testResult);
        //TODO: check mapping by testResult
    }

    @InputDirectory
    @PathSensitive(PathSensitivity.RELATIVE)
    public DirectoryProperty getMappingDirectory() {
        return mappingDirectory;
    }

    @InputDirectory
    @PathSensitive(PathSensitivity.RELATIVE)
    public DirectoryProperty getTestResultDirectory() {
        return testResultDirectory;
    }

}
