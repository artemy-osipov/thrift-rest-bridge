package ru.osipov.thrift.bridge.services

import org.junit.Test
import ru.osipov.thrift.bridge.domain.exception.NotFoundException

import static groovy.test.GroovyAssert.shouldFail
import static ru.osipov.thrift.bridge.TestData.*

class TServiceRepositoryTest {

    def repository = new TServiceRepository(THRIFT_PACKAGE)

    @Test
    void "list should return all services"() {
        def res = repository.list()

        assert res == services()
    }

    @Test
    void "findByName should filter service by name"() {
        def res = repository.findByName(SERVICE_NAME)

        assert res == service()
    }

    @Test
    void "findByName should fail when requested by unknown name"() {
        shouldFail(NotFoundException) {
            repository.findByName('unknown')
        }
    }
}