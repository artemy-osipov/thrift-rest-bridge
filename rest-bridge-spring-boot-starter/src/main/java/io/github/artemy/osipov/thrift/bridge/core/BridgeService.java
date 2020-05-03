package io.github.artemy.osipov.thrift.bridge.core;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.artemy.osipov.thrift.bridge.core.TService.TOperation;
import io.github.artemy.osipov.thrift.bridge.core.exception.ProxyInvocationException;
import lombok.RequiredArgsConstructor;
import org.apache.thrift.TBase;
import org.apache.thrift.TServiceClient;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
}
