package io.github.artemy.osipov.thrift.bridge.core.spec;

import lombok.Value;

@Value
public class SpecField {
    String name;
    SpecType type;
}
