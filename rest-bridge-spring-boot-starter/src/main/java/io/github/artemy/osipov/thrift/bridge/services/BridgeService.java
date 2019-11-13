package io.github.artemy.osipov.thrift.bridge.services;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.artemy.osipov.thrift.bridge.domain.exception.ProxyInvocationException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.thrift.TBase;
import org.apache.thrift.TServiceClient;
import org.springframework.stereotype.Service;
import io.github.artemy.osipov.thrift.bridge.domain.TOperation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BridgeService {

    private final ThriftConverter thriftConverter;

    public Object proxy(TOperation operation, String endpoint, JsonNode body) {
        TServiceClient thriftClient = operation.getService().buildThriftClient(endpoint);
        Method operationMethod = operation.buildClientMethod(thriftClient.getClass());

        return invokeThrift(thriftClient, operationMethod, thriftConverter.parseArgs(operationMethod, body));
    }

    private Object invokeThrift(TServiceClient thriftClient, Method operation, Object[] args) {
        try {
            return operation.invoke(thriftClient, args);
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

    public Object example(TOperation operation, int depth) {
        Parameter[] parameters = operation.buildClientMethod(operation.getService().getThriftServiceClass())
                .getParameters();

        return Arrays.stream(parameters)
                .collect(Collectors.toMap(
                        Parameter::getName,
                        p -> createSample(p.getType(), depth)
                ));
    }

    @SneakyThrows
    private Object createSample(Class<?> clazz, int depth) {
        return clazz.newInstance();
    }
}
