package io.github.artemy.osipov.thrift.bridge.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.artemy.osipov.thrift.bridge.controllers.BridgeController;
import io.github.artemy.osipov.thrift.bridge.controllers.ErrorHandler;
import io.github.artemy.osipov.thrift.bridge.core.BridgeService;
import io.github.artemy.osipov.thrift.bridge.core.TServiceRepository;
import io.github.artemy.osipov.thrift.bridge.core.ThriftConverter;
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
    public ThriftConverter thriftConverter(ObjectMapper mapper) {
        return new ThriftConverter(mapper);
    }

    @Bean
    public BridgeService bridgeService(ThriftConverter thriftConverter) {
        return new BridgeService(thriftConverter);
    }

    @Bean
    public TServiceRepository serviceRepository(BridgeProperties properties) {
        return new TServiceRepository(properties);
    }

    @Bean
    public BridgeController bridgeController(TServiceRepository repository, BridgeService bridgeService) {
        return new BridgeController(repository, bridgeService);
    }
}

