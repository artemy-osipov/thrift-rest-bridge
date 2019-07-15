package ru.osipov.thrift.bridge.controllers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.osipov.thrift.bridge.controllers.dto.ServiceDto;
import ru.osipov.thrift.bridge.domain.TService;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ModelMapper {

    ServiceDto map(TService src);
}
