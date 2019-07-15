package ru.osipov.thrift.bridge.services;

import org.springframework.stereotype.Repository;
import ru.osipov.thrift.bridge.domain.TService;

import java.util.Collection;
import java.util.Collections;

@Repository
public class TServiceRepository {

    public Collection<TService> list() {
        return Collections.emptyList();
    }

    public TService findByName(String name) {
        return null;
    }
}
