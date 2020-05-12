package io.github.artemy.osipov.thrift.bridge.core.spec

import com.google.gson.JsonParser
import org.junit.jupiter.api.Test

import static io.github.artemy.osipov.thrift.bridge.core.TestData.*

class TemplateSpecTest {

    def spec = new TemplateSpec()

    @Test
    void "should trim spec with depth level"() {
        def res = spec.format(proxyRequestSpecType(), 1)

        assert JsonParser.parseString(res) == JsonParser.parseString("""
                   {
                     "simpleField": null,
                     "complexField": null,
                     "listComplexField": null,
                     "setComplexField": null
                   }"""
        )
    }

    @Test
    void "should format full spec"() {
        def res = spec.format(proxyRequestSpecType())

        assert JsonParser.parseString(res) == JsonParser.parseString(templateSpec())
    }
}
