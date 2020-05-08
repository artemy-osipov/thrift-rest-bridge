package io.github.artemy.osipov.thrift.bridge.utils

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.artemy.osipov.thrift.jackson.ThriftModule

class JsonUtils {

    static final ObjectMapper MAPPER = new ObjectMapper().tap {
        registerModule(new ThriftModule())
    }

    static <T> String toJson(T object) {
        MAPPER.writeValueAsString(object)
    }

    static JsonNode toJsonNode(String json) {
        MAPPER.readTree(json)
    }

    static ObjectNode objectNode() {
        MAPPER.createObjectNode()
    }

    static ArrayNode arrayNode() {
        MAPPER.createArrayNode()
    }
}
