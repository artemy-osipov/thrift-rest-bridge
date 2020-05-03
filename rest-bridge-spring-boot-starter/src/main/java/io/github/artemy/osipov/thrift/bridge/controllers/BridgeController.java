package io.github.artemy.osipov.thrift.bridge.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.artemy.osipov.thrift.bridge.controllers.dto.ServiceDTO;
import io.github.artemy.osipov.thrift.bridge.core.BridgeService;
import io.github.artemy.osipov.thrift.bridge.core.TService;
import io.github.artemy.osipov.thrift.bridge.core.TService.TOperation;
import io.github.artemy.osipov.thrift.bridge.core.TServiceRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

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
        TOperation operation = service.operation(operationName);

        return bridgeService.proxy(operation, thriftEndpoint, body);
    }
}
