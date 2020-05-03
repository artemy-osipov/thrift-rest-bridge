package io.github.artemy.osipov.thrift.bridge.core;

import io.github.artemy.osipov.thrift.bridge.core.TService.TOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BridgeService {

    private final ArgumentParser parser;

    public Object proxy(TOperation operation, String endpoint, String body) {
        Object[] args = parser.parse(operation.getArgs(), body);
        return operation.proxy(endpoint, args);
    }
}
