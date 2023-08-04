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
