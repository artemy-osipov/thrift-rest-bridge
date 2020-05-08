package io.github.artemy.osipov.thrift.bridge.core.spec

import io.github.artemy.osipov.thrift.bridge.TestData
import io.github.artemy.osipov.thrift.bridge.utils.JsonUtils
import org.junit.jupiter.api.Test

class TemplateSpecTest {

    def spec = new TemplateSpec()

    @Test
    void "should trim spec with depth level"() {
        def res = spec.format(TestData.proxyRequestSpecType(), 1)

        assert JsonUtils.toJsonNode(res) == JsonUtils.toJsonNode("""
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
        def res = spec.format(TestData.proxyRequestSpecType())

        assert JsonUtils.toJsonNode(res) == JsonUtils.toJsonNode(TestData.templateSpec())
    }
}
