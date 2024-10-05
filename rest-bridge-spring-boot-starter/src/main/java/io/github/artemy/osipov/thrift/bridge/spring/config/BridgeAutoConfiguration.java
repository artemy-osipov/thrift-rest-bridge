package io.github.artemy.osipov.thrift.bridge.spring.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@EnableConfigurationProperties(BridgeProperties.class)
@Import(BridgeMvcConfiguration.class)
public class BridgeAutoConfiguration {
}
