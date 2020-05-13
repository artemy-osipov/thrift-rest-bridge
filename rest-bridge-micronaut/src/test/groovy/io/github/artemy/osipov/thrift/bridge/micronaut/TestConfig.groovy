package io.github.artemy.osipov.thrift.bridge.micronaut

import io.github.artemy.osipov.thrift.bridge.core.BridgeFacade
import io.github.artemy.osipov.thrift.bridge.core.TServiceRepository
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Primary

import javax.inject.Singleton

import static org.mockito.Mockito.mock

@Factory
class TestConfig {

    @Primary
    @Singleton
    BridgeFacade bridgeFacade() {
        mock(BridgeFacade)
    }

    @Primary
    @Singleton
    TServiceRepository serviceRepository() {
        mock(TServiceRepository)
    }
}