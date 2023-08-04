package io.github.projectleopold.producer.gradle.plugin.explorer.mapping;

import lombok.Data;

import java.util.Set;

@Data
public final class ContractMapping {
    private final Producer producer;
    private final Set<ConsumerWithContracts> consumers;

    @Data
    public static final class Producer {
        private final String group;
        private final String name;
        private final String version;
    }

    @Data
    public static class ConsumerWithContracts {
        private final Consumer consumer;
        private final Set<Contract> contracts;
    }

    @Data
    public static final class Consumer {
        private final String name;
    }

    @Data
    public static final class Contract {
        private final String filename;
        private final String methodName;
        private final String classSuffix;
    }
}
