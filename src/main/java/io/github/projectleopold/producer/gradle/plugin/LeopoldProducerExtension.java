package io.github.projectleopold.producer.gradle.plugin;

import org.gradle.api.Project;

public class LeopoldProducerExtension {

    public static final String EXTENSION_NAME = "leopoldProducer";

    public static LeopoldProducerExtension create(Project project) {
        return project.getExtensions().create(EXTENSION_NAME, LeopoldProducerExtension.class);
    }

}
