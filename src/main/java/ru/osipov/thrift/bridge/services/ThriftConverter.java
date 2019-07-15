package ru.osipov.thrift.bridge.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class ThriftConverter {

    private final ObjectMapper mapper;

    public Object[] parseArgs(Method method, JsonNode req) {
        return Arrays.stream(method.getParameters())
                .map(p -> mapper.convertValue(req.get(p.getName()), p.getType()))
                .toArray(Object[]::new);
    }

    @SneakyThrows
    public JsonNode parseResponse(Object resp) {
        if (resp == null) {
            return mapper.createObjectNode();
        }

        return mapper.valueToTree(resp);
    }
}
