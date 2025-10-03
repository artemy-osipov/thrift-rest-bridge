package io.github.artemy.osipov.thrift.bridge.core

import io.github.artemy.osipov.thrift.bridge.core.exception.NotFoundException
import io.github.artemy.osipov.thrift.bridge.test.TestService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

import static io.github.artemy.osipov.thrift.bridge.core.TestData.*

class TServiceTest {

    def service = new TService(THRIFT_CLIENT_CLASS)

    @Test
    void "should construct TService"() {
        assert service.name == SERVICE_NAME
        assert service.thriftServiceClass == THRIFT_CLIENT_CLASS
        assert service.operations == [(OPERATION_NAME): operation()]
    }

    @Test
    void "should return operation by name"() {
        def res = service.operation(OPERATION_NAME)

        assert res == operation()
    }

    @Test
    void "should fail return operation when requested by unknown name"() {
        Assertions.assertThrows(NotFoundException) {
            service.operation('unknown')
        }
    }

    @Test
    void "should build thrift client"() {
        def res = service.buildThriftClient(THRIFT_ENDPOINT)

        assert res instanceof TestService.Iface
    }
}