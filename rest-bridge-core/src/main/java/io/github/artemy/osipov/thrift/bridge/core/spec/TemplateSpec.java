package io.github.artemy.osipov.thrift.bridge.core.spec;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;

public class TemplateSpec {

    private final ObjectMapper mapper = new ObjectMapper();
    private final JsonNodeFactory nodeFactory = mapper.getNodeFactory();

    public String format(SpecType type) {
        return format(type, Integer.MAX_VALUE);
    }

    @SneakyThrows
    public String format(SpecType type, int depth) {
        return mapper.writeValueAsString(
                element(type, depth)
        );
    }

    private JsonNode element(SpecType type, int depth) {
        if (depth == 0) {
            return nodeFactory.nullNode();
        }

        switch (type.getType()) {
            case BOOLEAN:
                return nodeFactory.booleanNode(false);
            case NUMBER:
                return nodeFactory.numberNode(0);
            case STRING:
                return nodeFactory.textNode("");
            case ARRAY:
                return array(type.getContainerType(), depth);
            case OBJECT:
                return object(type.getNested(), depth);
            default:
                return nodeFactory.nullNode();
        }
    }

    private ArrayNode array(SpecType containerType, int depth) {
        return nodeFactory.arrayNode()
                .add(element(containerType, depth - 1));
    }

    private ObjectNode object(SpecField[] fields, int depth) {
        var node = nodeFactory.objectNode();
        for (SpecField field : fields) {
            node.set(field.getName(), element(field.getType(), depth - 1));
        }
        return node;
    }
}
