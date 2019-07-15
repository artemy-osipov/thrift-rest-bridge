package ru.osipov.thrift.bridge.domain;

import lombok.Data;
import lombok.SneakyThrows;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import ru.osipov.thrift.bridge.domain.exception.NotFoundException;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class TService {

    private final String name;
    private final Map<String, TOperation> operations;
    private final Class<? extends TServiceClient> thriftServiceClass;

    public TService(
            String name,
            Class<? extends TServiceClient> thriftServiceClass,
            Collection<TOperation> operations) {
        this.name = name;
        this.thriftServiceClass = thriftServiceClass;
        this.operations = operations.stream()
                .peek(o -> o.setService(this))
                .collect(Collectors.toMap(TOperation::getName, Function.identity()));
    }

    public TOperation getOperation(String operationName) {
        if (operations.containsKey(operationName)) {
            return operations.get(operationName);
        }

        throw new NotFoundException(String.format("No operation %s in service %s", operationName, this.name));
    }

    @SneakyThrows
    public TServiceClient buildThriftClient(String endpoint) {
        THttpClient thriftClient = new THttpClient(endpoint);
        TProtocol protocol = new TCompactProtocol(thriftClient);

        return thriftServiceClass.getConstructor(TProtocol.class)
                .newInstance(protocol);
    }
}
