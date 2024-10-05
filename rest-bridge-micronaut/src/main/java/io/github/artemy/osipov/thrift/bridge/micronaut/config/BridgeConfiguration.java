package io.github.artemy.osipov.thrift.bridge.micronaut.config;

import io.github.artemy.osipov.thrift.bridge.core.BridgeFacade;
import io.github.artemy.osipov.thrift.bridge.core.TServiceRepository;
import io.micronaut.context.annotation.Factory;

import jakarta.inject.Singleton;

@Factory
public class BridgeConfiguration {

    @Singleton
    public BridgeFacade bridgeFacade() {
        return new BridgeFacade();
    }

    @Singleton
    public TServiceRepository serviceRepository(BridgeProperties properties) {
        return new TServiceRepository(properties.getScanPackage());
    }
}

