package io.github.artemy.osipov.thrift.bridge.services

import io.github.artemy.osipov.thrift.bridge.domain.exception.NotFoundException
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringRunner
import io.github.artemy.osipov.thrift.bridge.UseConfigurationProperties
import io.github.artemy.osipov.thrift.bridge.config.BridgeProperties

import static groovy.test.GroovyAssert.shouldFail
import static io.github.artemy.osipov.thrift.bridge.TestData.*

@RunWith(SpringRunner)
@UseConfigurationProperties
class TServiceRepositoryTest {

    @Autowired
    BridgeProperties bridgeProperties

    TServiceRepository repository

    @Before
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