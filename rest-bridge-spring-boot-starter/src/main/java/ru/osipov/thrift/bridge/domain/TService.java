package ru.osipov.thrift.bridge.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import ru.osipov.thrift.bridge.domain.exception.NotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
public class TService {

    private static final String SERVICE_INTERFACE_NAME = "Iface";

    private final String name;
    private final Class<? extends TServiceClient> thriftServiceClass;
    private final Map<String, TOperation> operations;

    public static TService build(Class<? extends TServiceClient> clazz) {
        Class<?> serviceInterface = Arrays.stream(clazz.getInterfaces())
                .filter(i -> i.getName().endsWith(SERVICE_INTERFACE_NAME))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Class %s doesn't implements %s interface", clazz, SERVICE_INTERFACE_NAME)));

        List<TOperation> operations = Arrays.stream(serviceInterface.getDeclaredMethods())
                .map(m -> new TOperation(m.getName()))
                .collect(Collectors.toList());

        TService service = new TService(
                clazz.getEnclosingClass().getSimpleName(),
                clazz,
                operations.stream()
                        .collect(Collectors.toMap(TOperation::getName, Function.identity()))
        );

        operations.forEach(o -> o.setService(service));

        return service;
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
        TProtocol protocol = new TBinaryProtocol(thriftClient);

        return thriftServiceClass.getConstructor(TProtocol.class)
                .newInstance(protocol);
    }
}
