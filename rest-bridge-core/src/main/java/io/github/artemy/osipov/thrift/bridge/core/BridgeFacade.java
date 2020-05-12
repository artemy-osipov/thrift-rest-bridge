package io.github.artemy.osipov.thrift.bridge.core;

import io.github.artemy.osipov.thrift.bridge.core.TService.TOperation;
import io.github.artemy.osipov.thrift.bridge.core.spec.SpecType;
import io.github.artemy.osipov.thrift.bridge.core.spec.TemplateSpec;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BridgeFacade {

    public Object proxy(TOperation operation, String endpoint, String body) {
        TArguments arguments = operation.getArguments();
        return operation.proxy(endpoint, arguments.args(body));
    }

    public String template(TOperation operation, int depth) {
        TArguments arguments = operation.getArguments();
        SpecType specType = arguments.specType();
        return new TemplateSpec().format(specType, depth);
    }
}
