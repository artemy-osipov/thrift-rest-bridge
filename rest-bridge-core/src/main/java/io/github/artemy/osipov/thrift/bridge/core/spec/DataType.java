package io.github.artemy.osipov.thrift.bridge.core.spec;

public enum DataType {
    STRING, NUMBER, BOOLEAN, ARRAY, OBJECT;

    public boolean isPrimitive() {
        switch (this) {
            case STRING:
            case NUMBER:
            case BOOLEAN:
                return true;
            default:
                return false;
        }
    }
}
