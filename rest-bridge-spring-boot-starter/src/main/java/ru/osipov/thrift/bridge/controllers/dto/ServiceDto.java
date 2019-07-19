package ru.osipov.thrift.bridge.controllers.dto;

import lombok.Data;

import java.util.List;

@Data
public class ServiceDto {

    private String name;
    private List<String> operations;
}
