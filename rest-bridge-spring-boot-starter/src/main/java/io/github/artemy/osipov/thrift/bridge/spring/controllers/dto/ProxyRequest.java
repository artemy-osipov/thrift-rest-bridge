package io.github.artemy.osipov.thrift.bridge.spring.controllers.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class ProxyRequest {

    @NotBlank
    private String endpoint;
    private JsonNode body;
}
