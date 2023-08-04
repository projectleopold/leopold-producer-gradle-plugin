package io.github.projectleopold.producer.gradle.plugin.explorer.test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.github.projectleopold.producer.gradle.plugin.explorer.mapping.ContractMapping;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ContractTestExplorer {

    private final ContractMapping mapping;
    private final XmlMapper xmlMapper;

    public ContractTestExplorer(ContractMapping mapping) {
        this.mapping = mapping;
        this.xmlMapper = new XmlMapper();
        this.xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @SneakyThrows
    public ContractTestResult explore(Path testResults) {
        Set<String> prefixes = mapping.getConsumers().stream()
                .flatMap(consumer -> consumer.getContracts().stream())
                .map(contract -> contract.getClassSuffix() + ".xml")
                .collect(Collectors.toSet());
        List<ContractTestResult.TestSuite> testSuites;
        try (Stream<Path> stream = Files.list(testResults)) {
            testSuites = stream
                    //TODO: n^2
                    .filter(file -> prefixes.stream().anyMatch(prefix ->
                            file.getFileName().toString().toLowerCase().endsWith(prefix)))
                    .map(this::exploreTestSuite)
                    .toList();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return new ContractTestResult(testSuites);
    }

    @SneakyThrows
    private ContractTestResult.TestSuite exploreTestSuite(Path file) {
        return xmlMapper.readValue(file.toFile(), ContractTestResult.TestSuite.class);
    }

}
