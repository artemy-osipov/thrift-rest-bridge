package io.github.artemy.osipov.thrift.bridge.micronaut.config;

import io.micronaut.context.annotation.ConfigurationProperties;
import lombok.Data;

@Data
@ConfigurationProperties("bridge.thrift")
public class BridgeProperties {

    private String scanPackage;
}
