package ru.osipov.thrift.bridge.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.osipov.thrift.bridge.controllers.BridgeController;
import ru.osipov.thrift.bridge.controllers.ErrorHandler;
import ru.osipov.thrift.bridge.controllers.ThriftSerializer;
import ru.osipov.thrift.bridge.services.BridgeService;
import ru.osipov.thrift.bridge.services.TServiceRepository;
import ru.osipov.thrift.bridge.services.ThriftConverter;

@Configuration
@ConditionalOnWebApplication
@Import(ThriftSerializer.class)
public class BridgeMvcConfiguration {

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

