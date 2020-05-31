package io.github.artemy.osipov.thrift.bridge.core

import org.junit.jupiter.api.Test

import java.lang.reflect.Parameter

import static io.github.artemy.osipov.thrift.bridge.core.TestData.*
import static org.mockito.Mockito.*

class BridgeFacadeTest {

    def service = new BridgeFacade()
    def operation = mock(TService.TOperation)

    @Test
    void "should delegate proxy to operation"() {
        def arguments = new TArguments(new Parameter[0])
        doReturn(arguments)
                .when(operation)
                .getArguments()
        def resp = thriftComplexStruct()
        doReturn(resp)
                .when(operation)
                .proxy(eq(THRIFT_ENDPOINT), any())

        def res = service.proxy(operation, THRIFT_ENDPOINT, proxyRequestBody())

        assert res == resp
    }
}
