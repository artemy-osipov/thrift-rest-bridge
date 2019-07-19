package ru.osipov.thrift.bridge.services

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringRunner
import ru.osipov.thrift.bridge.UseConfigurationProperties
import ru.osipov.thrift.bridge.config.BridgeProperties
import ru.osipov.thrift.bridge.domain.exception.NotFoundException

import static groovy.test.GroovyAssert.shouldFail
import static ru.osipov.thrift.bridge.TestData.*

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