package io.github.artemy.osipov.thrift.bridge.core

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
        def res = operation.getArgs()

        assert res[0].name == 'simpleField'
        assert res[0].type == String
        assert res[1].name == 'complexField'
        assert res[1].type == TestStruct
    }

    @Test
    void "should proxy request to thrift"() {
        def resp = [thriftTestStruct()]
        doReturn(resp)
                .when(thriftClient)
                .testOperation(THRIFT_SIMPLE_FIELD, thriftTestStruct())

        def res = operation.proxy(THRIFT_ENDPOINT, proxyArgs())

        assert res == resp
    }

    @Test
    void "should proxy exception from thrift"() {
        def resp = thriftException()
        doThrow(resp)
                .when(thriftClient)
                .testOperation(THRIFT_SIMPLE_FIELD, thriftTestStruct())

        def res = operation.proxy(THRIFT_ENDPOINT, proxyArgs())

        assert res == resp
    }
}