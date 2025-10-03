package io.github.artemy.osipov.thrift.bridge.core

import io.github.artemy.osipov.thrift.bridge.core.exception.NotFoundException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

import static io.github.artemy.osipov.thrift.bridge.core.TestData.*

class TServiceRepositoryTest {

    def repository = new TServiceRepository('io.github.artemy.osipov.thrift.bridge.test')

    @Test
    void "list should return all services with operations"() {
        def res = repository.list()

        assert res == services()
    }

    @Test
    void "should filter service by id"() {
        def res = repository.findById(SERVICE_ID)

        assert res == service()
    }

    @Test
    void "should fail filter service by id when requested by unknown id"() {
        Assertions.assertThrows(NotFoundException) {
            repository.findById('unknown')
        }
    }
}