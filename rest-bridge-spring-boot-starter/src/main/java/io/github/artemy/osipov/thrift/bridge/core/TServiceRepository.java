package io.github.artemy.osipov.thrift.bridge.core;

import io.github.artemy.osipov.thrift.bridge.core.exception.NotFoundException;
import org.apache.thrift.TServiceClient;
import org.reflections.Reflections;
import org.springframework.stereotype.Repository;
import io.github.artemy.osipov.thrift.bridge.config.BridgeProperties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class TServiceRepository {

    private final Map<String, TService> serviceMap;

    public TServiceRepository(BridgeProperties properties) {
        serviceMap = new Reflections(properties.getScanPackage())
                .getSubTypesOf(TServiceClient.class)
                .stream()
                .map(TService::new)
                .collect(Collectors.toMap(TService::getId, Function.identity()));
    }

    public Collection<TService> list() {
        List<TService> services = new ArrayList<>(serviceMap.values());
        services.sort(Comparator.comparing(TService::getName));

        return services;
    }

    public TService findById(String id) {
        if (serviceMap.containsKey(id)) {
            return serviceMap.get(id);
        }

        throw new NotFoundException(String.format("Service %s not found", id));
    }
}
