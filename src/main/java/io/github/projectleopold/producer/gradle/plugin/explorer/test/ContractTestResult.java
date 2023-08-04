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
