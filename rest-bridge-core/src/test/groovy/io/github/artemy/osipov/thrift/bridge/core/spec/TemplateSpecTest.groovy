package io.github.artemy.osipov.thrift.bridge.core.spec

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test

import static io.github.artemy.osipov.thrift.bridge.core.TestData.*

class TemplateSpecTest {

    def spec = new TemplateSpec()
    def mapper = new ObjectMapper()

    @Test
    void "should trim spec with depth level"() {
        def res = spec.format(proxyRequestSpecType(), 1)

        assert mapper.readTree(res) == mapper.readTree("""
                   {
                     "simpleField": null,
                     "complexField": null,
                     "listStructField": null,
                     "setStructField": null
                   }"""
        )
    }

    @Test
    void "should format full spec"() {
        def res = spec.format(proxyRequestSpecType())

        assert mapper.readTree(res) == mapper.readTree(templateSpec())
    }
}
