package io.github.artemy.osipov.thrift.bridge.spring.utils

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.artemy.osipov.thrift.jackson.ThriftModule

class JsonUtils {

    static final ObjectMapper MAPPER = new ObjectMapper().tap {
        registerModule(new ThriftModule())
    }

    static <T> String toJson(T object) {
        MAPPER.writeValueAsString(object)
    }

    static String asNormalizedJson(String json) {
        MAPPER.readTree(json).toString()
    }
}
