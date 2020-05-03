package io.github.artemy.osipov.thrift.bridge.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.artemy.osipov.thrift.jackson.ThriftModule

class JsonUtils {

    static final ObjectMapper MAPPER = new ObjectMapper().tap {
        registerModule(new ThriftModule())
    }

    static <T> String toJson(T object) {
        MAPPER.writeValueAsString(object)
    }

    static ObjectNode objectNode() {
        MAPPER.createObjectNode()
    }
}
