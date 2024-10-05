package io.github.artemy.osipov.thrift.example;

import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

@MicronautTest
public class EndpointIT {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    public void shouldAddServicesEndpoint() {
        String res = client.toBlocking().retrieve("/services");

        Assertions.assertNotEquals("", res);
    }
}