package io.github.artemy.osipov.thrift.bridge.utils

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.mockito.ArgumentMatcher

class JsonMatcher implements ArgumentMatcher<JsonNode> {

    final JsonNode expected
    final ObjectMapper mapper = new ObjectMapper()

    JsonMatcher(JsonNode expected) {
        this.expected = expected
    }

    @Override
    boolean matches(JsonNode argument) {
        return mapper.writeValueAsString(expected) == mapper.writeValueAsString(argument)
    }
}
