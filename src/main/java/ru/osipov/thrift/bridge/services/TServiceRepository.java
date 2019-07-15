package ru.osipov.thrift.bridge.services;

import org.apache.thrift.TServiceClient;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import ru.osipov.thrift.bridge.domain.TService;
import ru.osipov.thrift.bridge.domain.exception.NotFoundException;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class TServiceRepository {

    private final Map<String, TService> serviceMap;

    public TServiceRepository(@Value("${bridge.thrift.package}") String thriftPackage) {
        serviceMap = new Reflections(thriftPackage)
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
