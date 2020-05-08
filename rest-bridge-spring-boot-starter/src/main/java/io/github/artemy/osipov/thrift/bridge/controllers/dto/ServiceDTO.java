package io.github.artemy.osipov.thrift.bridge.controllers.dto;

import lombok.Data;

import java.util.List;

@Data
public class ServiceDTO {

    private String id;
    private String name;
    private List<OperationDTO> operations;

    @Data
    public static class OperationDTO {
        private String name;
    }
}
