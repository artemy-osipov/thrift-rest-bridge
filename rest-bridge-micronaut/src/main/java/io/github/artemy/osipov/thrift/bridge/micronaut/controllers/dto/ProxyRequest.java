package io.github.artemy.osipov.thrift.bridge.micronaut.controllers.dto;

import com.fasterxml.jackson.databind.JsonNode;
import io.micronaut.core.annotation.Introspected;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Introspected
public class ProxyRequest {

    @NotBlank
    private String endpoint;
    private JsonNode body;
}
