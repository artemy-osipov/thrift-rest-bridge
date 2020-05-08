package io.github.artemy.osipov.thrift.bridge.core

import io.github.artemy.osipov.thrift.bridge.test.TestInnerStruct
import io.github.artemy.osipov.thrift.bridge.test.TestService
import io.github.artemy.osipov.thrift.bridge.test.TestStruct
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import static io.github.artemy.osipov.thrift.bridge.TestData.*
import static org.mockito.Mockito.*

class TOperationTest {

    def thriftClient = mock(TestService.Client)
    def thriftClientFactory = mock(ThriftClientFactory)
    def operation = new TService(THRIFT_CLIENT_CLASS, thriftClientFactory).operation(OPERATION_NAME)

    @BeforeEach
    void setup() {
        doReturn(thriftClient)
                .when(thriftClientFactory)
                .build(THRIFT_CLIENT_CLASS, THRIFT_ENDPOINT)
    }

    @Test
    void "should return args"() {
        def res = operation.getArguments()

        assert res.parameters[0].name == 'simpleField'
        assert res.parameters[0].type == String
        assert res.parameters[1].name == 'complexField'
        assert res.parameters[1].type == TestStruct
        assert res.parameters[2].name == 'listComplexField'
        assert res.parameters[2].type == List<TestInnerStruct>
        assert res.parameters[3].name == 'setComplexField'
        assert res.parameters[3].type == Set<TestInnerStruct>
    }

    @Test
    void "should proxy request to thrift"() {
        def resp = [thriftTestStruct()]
        doReturn(resp)
                .when(thriftClient)
                .testOperation(THRIFT_SIMPLE_FIELD, thriftTestStruct(), [thriftTestInnerStruct()], Set.of(thriftTestInnerStruct()))

        def res = operation.proxy(THRIFT_ENDPOINT, proxyArgs())

        assert res == resp
    }

    @Test
    void "should proxy exception from thrift"() {
        def resp = thriftException()
        doThrow(resp)
                .when(thriftClient)
                .testOperation(THRIFT_SIMPLE_FIELD, thriftTestStruct(), [thriftTestInnerStruct()], Set.of(thriftTestInnerStruct()))

        def res = operation.proxy(THRIFT_ENDPOINT, proxyArgs())

        assert res == resp
    }
}