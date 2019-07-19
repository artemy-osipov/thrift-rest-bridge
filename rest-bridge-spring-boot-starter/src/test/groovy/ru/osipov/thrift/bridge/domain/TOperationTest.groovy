package ru.osipov.thrift.bridge.domain

import org.junit.Test
import ru.osipov.thrift.bridge.domain.exception.NotFoundException
import ru.osipov.thrift.bridge.test.AnotherTestService
import ru.osipov.thrift.bridge.test.TestService

import static groovy.test.GroovyAssert.shouldFail
import static ru.osipov.thrift.bridge.TestData.operation

class TOperationTest {

    def operation = operation()

    @Test
    void "findClientMethod should filter method by name"() {
        def res = operation.buildClientMethod(TestService.Client)

        assert res.name == operation.name
    }

    @Test
    void "findClientMethod should fail when requested by unknown name"() {
        shouldFail(NotFoundException) {
            operation.buildClientMethod(AnotherTestService.Client)
        }
    }
}