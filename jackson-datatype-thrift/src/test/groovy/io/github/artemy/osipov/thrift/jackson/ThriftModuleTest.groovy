package io.github.artemy.osipov.thrift.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Test
import io.github.artemy.osipov.thrift.bridge.test.TestEnum
import io.github.artemy.osipov.thrift.bridge.test.TestInnerStruct
import io.github.artemy.osipov.thrift.bridge.test.TestStruct

class ThriftModuleTest {

    def mapper = new ObjectMapper().tap {
        registerModule(new ThriftModule())
    }

    @Test
    void "thrift module should trim thrift specific fields"() {
        def res = mapper.valueToTree([thriftTestStruct(), thriftTestStruct()])

        assert res == mapper.createArrayNode()
                .add(restTestStruct())
                .add(restTestStruct())
    }

    def thriftTestStruct() {
        new TestStruct().tap {
            stringField = 'some'
            boolField = true
            intField = 42
            enumField = TestEnum.ENUM_2
            complexField = new TestInnerStruct('f1', 'f2')
        }
    }

    def restTestStruct() {
        def thrift = thriftTestStruct()
        mapper.createObjectNode()
                .put('stringField', thrift.stringField)
                .put('boolField', thrift.boolField)
                .put('intField', thrift.intField)
                .put('enumField', thrift.enumField.name())
                .set('complexField',
                        mapper.createObjectNode()
                                .put('f1', thrift.complexField.f1)
                                .put('f2', thrift.complexField.f2)
                )
    }
}