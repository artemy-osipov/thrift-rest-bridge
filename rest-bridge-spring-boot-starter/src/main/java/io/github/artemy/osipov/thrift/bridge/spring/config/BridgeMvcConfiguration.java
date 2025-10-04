package io.github.artemy.osipov.thrift.bridge.spring.config;

import io.github.artemy.osipov.thrift.bridge.core.ThriftModelArtifactRepository;
import io.github.artemy.osipov.thrift.bridge.spring.controllers.BridgeController;
import io.github.artemy.osipov.thrift.bridge.spring.controllers.ErrorHandler;
import io.github.artemy.osipov.thrift.bridge.core.BridgeFacade;
import io.github.artemy.osipov.thrift.bridge.core.TServiceRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.github.artemy.osipov.thrift.jackson.ThriftModule;
import org.springframework.context.annotation.Import;
import org.springframework.util.StringUtils;

@Configuration
@ConditionalOnWebApplication
@Import(BridgeController.class)
public class BridgeMvcConfiguration {

    @Bean
    public ThriftModule thriftModule() {
        return new ThriftModule();
    }

    @Bean
    public ErrorHandler errorHandler() {
        return new ErrorHandler();
    }

    @Bean
    public BridgeFacade bridgeFacade() {
        return new BridgeFacade();
    }

    @Bean
    public TServiceRepository serviceRepository(BridgeProperties properties) {
        return new TServiceRepository(properties.getScanPackage());
    }

    @Bean
    public ThriftModelArtifactRepository thriftModelArtifactHolder(BridgeProperties properties) {
        if (properties.getArtifact() == null
                || !StringUtils.hasText(properties.getArtifact().getGroupId())) {
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

