package io.github.artemy.osipov.thrift.bridge.core.spec;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.EnumMap;
import java.util.Map;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SpecType {

    private static final Map<DataType, SpecType> PRIMITIVES = new EnumMap<>(DataType.class);

    DataType type;
    SpecType containerType;
    SpecField[] nested;

    public static SpecType object(SpecField... fields) {
        return new SpecType(DataType.OBJECT, null, fields);
    }

    public static SpecType primitive(DataType type) {
        if (!type.isPrimitive()) {
            throw new IllegalArgumentException(type + " is not primitive");
        }

        return PRIMITIVES.computeIfAbsent(type, key -> new SpecType(key, null, null));
    }

    public static SpecType array(SpecType containerType) {
        return new SpecType(DataType.ARRAY, containerType, null);
    }

    public String toString() {
        return "SpecType(type=" + type + ", containerType=" + containerType + ", nested=" + nested.length + " count)";
    }
}
