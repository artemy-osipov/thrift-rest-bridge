package io.github.artemy.osipov.thrift.bridge.core;

import io.github.artemy.osipov.thrift.bridge.core.exception.NotFoundException;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.apache.thrift.TServiceClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TServiceRepository {

    private final String basePackage;
    private Map<String, TService> serviceMap;

    public TServiceRepository(String basePackage) {
        this.basePackage = basePackage;
        reload(Thread.currentThread().getContextClassLoader());
    }

    public void reload(ClassLoader loader) {
        try (ScanResult result = new ClassGraph()
                .enableClassInfo()
                .acceptPackages(basePackage)
                .overrideClassLoaders(loader)
                .scan()) {
            serviceMap = result
                    .getSubclasses(TServiceClient.class)
                    .loadClasses()
                    .stream()
                    .map(clazz -> new TService((Class<? extends TServiceClient>) clazz))
                    .filter(s -> !s.getOperations().isEmpty())
                    .collect(Collectors.toMap(TService::getId, Function.identity()));
        }
    }

    public void reload(ThriftModelArtifactRepository repository) {
        if (!repository.hasArtifact()) {
            return;
        }
        repository.fetchLatest();
        reload(repository.getClassLoader());
    }

    public Collection<TService> list() {
        List<TService> services = new ArrayList<>(serviceMap.values());
        services.sort(Comparator.comparing(TService::getName));

        return services;
    }

    public TService findById(String id) {
        if (!serviceMap.containsKey(id)) {
            throw new NotFoundException(String.format("Service %s not found", id));
        }

        return serviceMap.get(id);
    }
}
