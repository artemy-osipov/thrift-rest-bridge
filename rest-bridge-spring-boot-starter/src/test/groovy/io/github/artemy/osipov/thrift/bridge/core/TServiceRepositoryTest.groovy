package io.github.artemy.osipov.thrift.bridge.core

import io.github.artemy.osipov.thrift.bridge.core.exception.NotFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import io.github.artemy.osipov.thrift.bridge.UseConfigurationProperties
import io.github.artemy.osipov.thrift.bridge.config.BridgeProperties

import static groovy.test.GroovyAssert.shouldFail
import static io.github.artemy.osipov.thrift.bridge.TestData.*

@UseConfigurationProperties
class TServiceRepositoryTest {

    @Autowired
    BridgeProperties bridgeProperties

    TServiceRepository repository

    @BeforeEach
    void setup() {
        repository = new TServiceRepository(bridgeProperties)
    }

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