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

package io.github.projectleopold.producer.gradle.plugin;

import io.github.projectleopold.producer.gradle.plugin.spring.SpringCloudContractPluginHelper;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

public class LeopoldProducerPlugin implements Plugin<Project> {

    public static final String PLUGIN_ID = "io.github.projectleopold.producer";

    private static final Logger LOGGER = Logging.getLogger(LeopoldProducerPlugin.class);

    @Override
    public void apply(Project project) {
        LeopoldProducerExtension.create(project);
        project.getPlugins().withId(SpringCloudContractPluginHelper.PLUGIN_ID, this::withSpringCloudContractPlugin);
        project.afterEvaluate(this::afterEvaluate);
    }

    private void withSpringCloudContractPlugin(Plugin<?> plugin) {
        requireCompatibleVersionOfSpringCloudContractsPlugin(plugin);
    }

    private void afterEvaluate(Project project) {
        requireSpringCloudContractsPlugin(project);
    }

    private void requireSpringCloudContractsPlugin(Project project) {
        if (!project.getPlugins().hasPlugin(SpringCloudContractPluginHelper.PLUGIN_ID)) {
            throw new IllegalStateException("No plugin '" + SpringCloudContractPluginHelper.PLUGIN_ID + "'");
        }
    }

    private void requireCompatibleVersionOfSpringCloudContractsPlugin(Plugin<?> plugin) {
        String springPluginVersion = plugin.getClass().getPackage().getImplementationVersion();
        if (!springPluginVersion.startsWith("4.0.")) {
            throw new IllegalStateException("Unsupported plugin version '" +
                    SpringCloudContractPluginHelper.PLUGIN_ID + ":" + springPluginVersion + "'" +
                    ", supported only versions '4.0.x'");
        }
    }

}
