package io.github.artemy.osipov.thrift.bridge;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ContextConfiguration;
import io.github.artemy.osipov.thrift.bridge.config.BridgeProperties;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ContextConfiguration(
        classes = UseConfigurationProperties.ConfigurationPropertiesConfiguration.class,
        initializers = ConfigFileApplicationContextInitializer.class)
public @interface UseConfigurationProperties {

    @TestConfiguration
    @EnableConfigurationProperties({
            BridgeProperties.class
    })
    class ConfigurationPropertiesConfiguration {
    }
}
