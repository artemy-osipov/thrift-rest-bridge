package ru.osipov.thrift.bridge.controllers;

import org.mapstruct.*;
import ru.osipov.thrift.bridge.controllers.dto.ServiceDto;
import ru.osipov.thrift.bridge.domain.TOperation;
import ru.osipov.thrift.bridge.domain.TService;

import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ModelMapper {

    @Mapping(target = "operations", ignore = true)
    ServiceDto map(TService src);

    @AfterMapping
    default void fillService(TService src, @MappingTarget ServiceDto result) {
        result.setOperations(
                src.getOperations()
                        .values()
                        .stream()
                        .map(TOperation::getName)
                        .sorted()
                        .collect(Collectors.toList())
        );
    }
}
