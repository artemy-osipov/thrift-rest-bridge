package io.github.artemy.osipov.thrift.bridge.core;

import io.github.artemy.osipov.thrift.bridge.core.exception.NotFoundException;
import io.github.artemy.osipov.thrift.bridge.core.exception.ProxyInvocationException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.ToString;
import org.apache.thrift.TBase;
import org.apache.thrift.TServiceClient;

import java.lang.reflect.InvocationTargetException;
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
                .orElseThrow(() -> new NotFoundException(
                        String.format("Class %s doesn't implements %s interface", clazz, SERVICE_INTERFACE_NAME)));

        this.name = clazz.getEnclosingClass().getSimpleName();
        this.thriftServiceClass = clazz;
        this.thriftClientFactory = thriftClientFactory;
        this.operations = Arrays.stream(serviceInterface.getDeclaredMethods())
                .map(m -> new TOperation(m.getName(), m))
                .collect(Collectors.toMap(TOperation::getName, Function.identity()));
    }

    public String getId() {
        return thriftServiceClass.getCanonicalName();
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
    @EqualsAndHashCode(exclude = "method")
    @ToString(exclude = "method")
    public class TOperation {

        private final String name;
        private final Method method;

        public TArguments getArguments() {
            return new TArguments(method.getParameters());
        }

        public Object proxy(String endpoint, Object[] args) {
            TServiceClient thriftClient = buildThriftClient(endpoint);

            return invokeThrift(thriftClient, args);
        }

        private Object invokeThrift(TServiceClient thriftClient, Object[] args) {
            try {
                return method.invoke(thriftClient, args);
            } catch (IllegalAccessException e) {
                throw new ProxyInvocationException(e);
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof TBase) {
                    return e.getCause();
                } else {
                    throw new ProxyInvocationException(e.getCause());
                }
            }
        }
    }
}
