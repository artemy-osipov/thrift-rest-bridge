package io.github.artemy.osipov.thrift.bridge.core;

import lombok.SneakyThrows;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;

public class ThriftClientFactory {

    @SneakyThrows
    public TServiceClient build(Class<? extends TServiceClient> clazz, String endpoint) {
        THttpClient client = new THttpClient(endpoint);
        TProtocol protocol = new TBinaryProtocol(client);

        return clazz.getConstructor(TProtocol.class).newInstance(protocol);
    }
}
