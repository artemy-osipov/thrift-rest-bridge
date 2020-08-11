package io.github.artemy.osipov.thrift.bridge.core.spec;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DataType {
    ENUM(true),
    STRING(true),
    NUMBER(true),
    BOOLEAN(true),
    ARRAY(false),
    OBJECT(false);

    private final boolean primitive;
}
