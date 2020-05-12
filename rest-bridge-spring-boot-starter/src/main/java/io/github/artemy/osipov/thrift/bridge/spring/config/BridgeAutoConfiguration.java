package io.github.artemy.osipov.thrift.bridge.spring.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties(BridgeProperties.class)
@Import(BridgeMvcConfiguration.class)
public class BridgeAutoConfiguration {
}
