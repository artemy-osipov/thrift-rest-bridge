package io.github.artemy.osipov.thrift.bridge.micronaut.controllers.dto;

import lombok.Data;

import java.util.List;

@Data
public class Service {

    private String id;
    private String name;
    private List<Operation> operations;

    @Data
    public static class Operation {
        private String name;
    }
}
