package io.github.artemy.osipov.thrift.bridge.micronaut.controllers.dto;

import io.micronaut.core.annotation.Introspected;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Introspected
public class ProxyRequest {

    @NotBlank
    private String endpoint;
    private String body;
}
