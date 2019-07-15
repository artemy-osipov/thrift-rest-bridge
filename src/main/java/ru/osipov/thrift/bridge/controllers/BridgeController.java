package ru.osipov.thrift.bridge.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.*;
import ru.osipov.thrift.bridge.controllers.dto.ServiceDto;
import ru.osipov.thrift.bridge.domain.TOperation;
import ru.osipov.thrift.bridge.domain.TService;
import ru.osipov.thrift.bridge.services.BridgeService;
import ru.osipov.thrift.bridge.services.TServiceRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class BridgeController {

    private final ModelMapper modelMapper = Mappers.getMapper(ModelMapper.class);
    private final TServiceRepository serviceRepository;
    private final BridgeService bridgeService;

    @GetMapping("/services")
    public Collection<ServiceDto> services() {
        return serviceRepository.list()
                .stream()
                .map(modelMapper::map)
                .collect(Collectors.toList());
    }

    @GetMapping("/services/{serviceName}")
    public ServiceDto findService(@PathVariable String serviceName) {
        return modelMapper.map(
                serviceRepository.findByName(serviceName)
        );
    }

    @PostMapping("/services/{serviceName}/operations/{operationName}")
    public JsonNode proxy(
            @PathVariable String serviceName,
            @PathVariable String operationName,
            @RequestHeader("Thrift-Endpoint") String thriftEndpoint,
            @RequestBody JsonNode body) {
        TService service = serviceRepository.findByName(serviceName);
        TOperation operation = service.getOperation(operationName);

        return bridgeService.proxy(operation, thriftEndpoint, body);
    }
}
