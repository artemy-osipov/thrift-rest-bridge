package io.github.artemy.osipov.thrift.bridge.core;

import io.github.artemy.osipov.thrift.bridge.core.exception.NotFoundException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.ToString;
import org.apache.thrift.TServiceClient;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(exclude = "thriftClientFactory")
@ToString(exclude = "thriftClientFactory")
public class TService {

    private static final String SERVICE_INTERFACE_NAME = "Iface";

    private final String name;
    private final Class<? extends TServiceClient> thriftServiceClass;
    private final ThriftClientFactory thriftClientFactory;
    private final Map<String, TOperation> operations;

    public TService(Class<? extends TServiceClient> clazz) {
        this(clazz, new ThriftClientFactory());
    }

    public TService(Class<? extends TServiceClient> clazz, ThriftClientFactory thriftClientFactory) {
        Class<?> serviceInterface = Arrays.stream(clazz.getInterfaces())
                .filter(i -> i.getName().endsWith(SERVICE_INTERFACE_NAME))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Class %s doesn't implements %s interface", clazz, SERVICE_INTERFACE_NAME)));

        this.name = clazz.getEnclosingClass().getSimpleName();
        this.thriftServiceClass = clazz;
        this.thriftClientFactory = thriftClientFactory;
        this.operations = Arrays.stream(serviceInterface.getDeclaredMethods())
                .map(m -> new TOperation(m.getName()))
                .collect(Collectors.toMap(TOperation::getName, Function.identity()));
    }

    public TOperation operation(String operationName) {
        if (operations.containsKey(operationName)) {
            return operations.get(operationName);
        }

        throw new NotFoundException(String.format("No operation %s in service %s", operationName, this.name));
    }

    @SneakyThrows
    public TServiceClient buildThriftClient(String endpoint) {
        return thriftClientFactory.build(thriftServiceClass, endpoint);
    }

    @Data
    public class TOperation {

        private final String name;

        public TService getService() {
            return TService.this;
        }

        public Method buildClientMethod(Class<?> clientClass) {
            return Arrays.stream(clientClass.getMethods())
                    .filter(m -> m.getName().equals(name))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException(String.format("No method %s in client %s", name, clientClass.getName())));
        }
    }
}
