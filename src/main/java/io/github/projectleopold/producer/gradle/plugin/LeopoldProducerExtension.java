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

import io.github.projectleopold.producer.gradle.plugin.task.InformLeopoldByProducerTask;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

public class LeopoldProducerExtension {

    public static final String EXTENSION_NAME = "leopoldProducer";

    private static final Logger LOGGER = Logging.getLogger(InformLeopoldByProducerTask.class);

    public static LeopoldProducerExtension create(Project project) {
        LeopoldProducerExtension extension = project.getExtensions().create(EXTENSION_NAME, LeopoldProducerExtension.class);
        LOGGER.debug("Created extension: {}", EXTENSION_NAME);
        return extension;
    }

}
