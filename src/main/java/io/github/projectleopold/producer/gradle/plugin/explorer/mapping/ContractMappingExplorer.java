package io.github.projectleopold.producer.gradle.plugin.explorer.mapping;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record ContractMappingExplorer() {

    public ContractMapping explore(Path mappings) {
        Set<ContractMapping.ConsumerWithContracts> contracts;
        try (Stream<Path> stream = Files.list(mappings)) {
            contracts = stream
                    .filter(Files::isDirectory)
                    .map(this::exploreConsumer)
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        String version = mappings.getParent().getFileName().toString();
        String name = mappings.getParent().getParent().getFileName().toString();
        String group = mappings.getParent().getParent().getParent().getFileName().toString();
        ContractMapping.Producer producer = new ContractMapping.Producer(group, name, version);
        return new ContractMapping(producer, contracts);
    }

    private ContractMapping.ConsumerWithContracts exploreConsumer(Path consumer) {
        Set<ContractMapping.Contract> contracts;
        try (Stream<Path> stream = Files.walk(consumer)) {
            contracts = stream
                    .filter(Files::isRegularFile)
                    .map(exploreContract(consumer))
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        String consumerName = consumer.getFileName().toString();
        return new ContractMapping.ConsumerWithContracts(new ContractMapping.Consumer(consumerName), contracts);
    }

    private Function<Path, ContractMapping.Contract> exploreContract(Path consumer) {
        String prefix = consumer.getParent().toString();
        int skip = prefix.length() + 1;
        return contract -> {
            String filename = contract.getFileName().toString();
            String methodName = "validate_" + filename.substring(0, filename.indexOf('.'))
                    .replace('-', '_');
            String classSuffix = contract.getParent().toString().substring(skip)
                    .replace('/', '.')
                    .replace('\\', '.')
                    .replace('-', '_')
                    .toLowerCase() + "test";
            return new ContractMapping.Contract(filename, methodName, classSuffix);
        };
    }

}
