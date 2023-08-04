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
