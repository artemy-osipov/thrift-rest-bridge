package io.github.artemy.osipov.thrift.bridge.services;

import io.github.artemy.osipov.thrift.bridge.domain.exception.NotFoundException;
import org.apache.thrift.TServiceClient;
import org.reflections.Reflections;
import org.springframework.stereotype.Repository;
import io.github.artemy.osipov.thrift.bridge.config.BridgeProperties;
import io.github.artemy.osipov.thrift.bridge.domain.TService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class TServiceRepository {

    private final Map<String, TService> serviceMap;

    public TServiceRepository(BridgeProperties properties) {
        serviceMap = new Reflections(properties.getScanPackage())
                .getSubTypesOf(TServiceClient.class)
                .stream()
                .map(TService::build)
                .collect(Collectors.toMap(TService::getName, Function.identity()));
    }

    public Collection<TService> list() {
        List<TService> services = new ArrayList<>(serviceMap.values());
        services.sort(Comparator.comparing(TService::getName));

        return services;
    }

    public TService findByName(String name) {
        if (serviceMap.containsKey(name)) {
            return serviceMap.get(name);
        }

        throw new NotFoundException(String.format("Service %s not found", name));
    }
}
