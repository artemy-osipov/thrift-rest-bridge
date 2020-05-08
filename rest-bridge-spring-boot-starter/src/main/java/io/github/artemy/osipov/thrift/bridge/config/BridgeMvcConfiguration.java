package io.github.artemy.osipov.thrift.bridge.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.artemy.osipov.thrift.bridge.controllers.BridgeController;
import io.github.artemy.osipov.thrift.bridge.controllers.ErrorHandler;
import io.github.artemy.osipov.thrift.bridge.core.BridgeFacade;
import io.github.artemy.osipov.thrift.bridge.core.TServiceRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.github.artemy.osipov.thrift.jackson.ThriftModule;

@Configuration
@ConditionalOnWebApplication
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
        return new TServiceRepository(properties);
    }

    @Bean
    public BridgeController bridgeController(TServiceRepository repository, BridgeFacade bridgeFacade, ObjectMapper objectMapper) {
        return new BridgeController(repository, bridgeFacade, objectMapper);
    }
}

