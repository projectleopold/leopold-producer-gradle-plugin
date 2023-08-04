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

package io.github.projectleopold.producer.gradle.plugin.explorer.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.Data;

import java.util.List;

@Data
public class ContractTestResult {
    private final List<TestSuite> testSuites;

    @Data
    public static class TestSuite {
        private String name;

        @JsonProperty("testcase")
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<TestCase> testCases;
    }

    @Data
    public static class TestCase {
        private String name;
        private TestcaseFailure failure;
    }

    @Data
    public static class TestcaseFailure {
        private String message;
    }
}
