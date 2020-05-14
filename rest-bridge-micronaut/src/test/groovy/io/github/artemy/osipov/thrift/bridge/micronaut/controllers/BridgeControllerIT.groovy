package io.github.artemy.osipov.thrift.bridge.micronaut.controllers

import io.github.artemy.osipov.thrift.bridge.core.BridgeFacade
import io.github.artemy.osipov.thrift.bridge.core.TServiceRepository
import io.github.artemy.osipov.thrift.bridge.spec.BridgeSpec
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.annotation.MicronautTest
import org.junit.jupiter.api.BeforeEach

import javax.inject.Inject

@MicronautTest
class BridgeControllerIT extends BridgeSpec {

    @Inject
    EmbeddedServer server

    @Inject
    TServiceRepository serviceRepository

    @Inject
    BridgeFacade bridgeFacade

    @BeforeEach
    void setup() {
        init(
                server.port,
                serviceRepository,
                bridgeFacade
        )
    }
}