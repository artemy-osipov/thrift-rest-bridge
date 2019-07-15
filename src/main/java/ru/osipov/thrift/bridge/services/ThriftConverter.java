package ru.osipov.thrift.bridge.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

@Component
public class ThriftConverter {

    public Object[] parseArgs(JsonNode req) {
        return null;
    }

    public JsonNode parseResponse(Object resp) {
        return null;
    }
}
