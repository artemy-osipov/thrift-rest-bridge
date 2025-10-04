package io.github.artemy.osipov.thrift.bridge.micronaut.config;

import io.github.artemy.osipov.thrift.bridge.core.BridgeFacade;
import io.github.artemy.osipov.thrift.bridge.core.ThriftModelArtifactRepository;
import io.github.artemy.osipov.thrift.bridge.core.TServiceRepository;
import io.micronaut.context.annotation.Factory;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
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

    @Singleton
    public ThriftModelArtifactRepository thriftModelArtifactHolder(BridgeProperties properties) {
        if (properties.getArtifact() == null
                || StringUtils.isEmpty(properties.getArtifact().getGroupId())) {
            return new ThriftModelArtifactRepository(null);
        }
        return new ThriftModelArtifactRepository(
                new ThriftModelArtifactRepository.ThriftModelArtifact(
                        properties.getArtifact().getRepositories(),
                        properties.getArtifact().getGroupId(),
                        properties.getArtifact().getArtifactId()
                )
        );
    }
}

