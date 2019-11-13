package io.github.artemy.osipov.thrift.bridge.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("bridge.thrift")
public class BridgeProperties {

    private String scanPackage;
}
