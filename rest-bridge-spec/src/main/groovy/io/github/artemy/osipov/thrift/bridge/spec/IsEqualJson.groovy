package io.github.artemy.osipov.thrift.bridge.spec

import groovy.json.JsonSlurper
import org.mockito.ArgumentMatcher

class IsEqualJson implements ArgumentMatcher<String> {

    private final String json

    IsEqualJson(String json) {
        this.json = json
    }

    static ArgumentMatcher<String> json(String json) {
        new IsEqualJson(json)
    }

    @Override
    boolean matches(String arg) {
        new JsonSlurper().parseText(arg) == new JsonSlurper().parseText(json)
    }
}
