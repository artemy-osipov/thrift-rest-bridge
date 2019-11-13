package io.github.artemy.osipov.thrift.bridge.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.*;
import io.github.artemy.osipov.thrift.bridge.controllers.dto.ServiceDTO;
import io.github.artemy.osipov.thrift.bridge.domain.TOperation;
import io.github.artemy.osipov.thrift.bridge.domain.TService;
import io.github.artemy.osipov.thrift.bridge.services.BridgeService;
import io.github.artemy.osipov.thrift.bridge.services.TServiceRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class BridgeController {

    private final ModelMapper modelMapper = Mappers.getMapper(ModelMapper.class);
    private final TServiceRepository serviceRepository;
    private final BridgeService bridgeService;

    @GetMapping("/services")
    public Collection<ServiceDTO> services() {
        return serviceRepository.list()
                .stream()
                .map(modelMapper::map)
                .collect(Collectors.toList());
    }

    @GetMapping("/services/{serviceName}")
    public ServiceDTO findService(@PathVariable String serviceName) {
        return modelMapper.map(
                serviceRepository.findByName(serviceName)
        );
    }

    @PostMapping("/services/{serviceName}/operations/{operationName}")
    public Object proxy(
            @PathVariable String serviceName,
            @PathVariable String operationName,
            @RequestHeader("Thrift-Endpoint") String thriftEndpoint,
            @RequestBody JsonNode body) {
        TService service = serviceRepository.findByName(serviceName);
        TOperation operation = service.getOperation(operationName);

        return bridgeService.proxy(operation, thriftEndpoint, body);
    }

    @GetMapping("/services/{serviceName}/operations/{operationName}")
    public Object getExample(
            @PathVariable String serviceName,
            @PathVariable String operationName,
            @RequestParam(name = "depth", defaultValue = "3") int depth) {
        TService service = serviceRepository.findByName(serviceName);
        TOperation operation = service.getOperation(operationName);

        return bridgeService.example(operation, depth);
    }
}
