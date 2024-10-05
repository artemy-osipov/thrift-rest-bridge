package io.github.artemy.osipov.thrift.bridge.spring.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProxyResponse {

    private Object response;
}
