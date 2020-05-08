package io.github.artemy.osipov.thrift.bridge

import com.fasterxml.jackson.databind.JsonNode
import groovy.transform.CompileStatic
import io.github.artemy.osipov.thrift.bridge.controllers.dto.ProxyRequestDTO
import io.github.artemy.osipov.thrift.bridge.core.TService
import io.github.artemy.osipov.thrift.bridge.core.TService.TOperation
import io.github.artemy.osipov.thrift.bridge.core.spec.DataType
import io.github.artemy.osipov.thrift.bridge.core.spec.SpecField
import io.github.artemy.osipov.thrift.bridge.core.spec.SpecType
import io.github.artemy.osipov.thrift.bridge.utils.JsonUtils
import org.apache.thrift.TServiceClient
import io.github.artemy.osipov.thrift.bridge.test.AnotherTestService
import io.github.artemy.osipov.thrift.bridge.test.ErrorInfo
import io.github.artemy.osipov.thrift.bridge.test.SubTestService
import io.github.artemy.osipov.thrift.bridge.test.TestEnum
import io.github.artemy.osipov.thrift.bridge.test.TestException
import io.github.artemy.osipov.thrift.bridge.test.TestInnerStruct
import io.github.artemy.osipov.thrift.bridge.test.TestService
import io.github.artemy.osipov.thrift.bridge.test.TestStruct

@CompileStatic
class TestData {

    static String THRIFT_ENDPOINT = 'http://some.com/tapi'
    static Class<? extends TServiceClient> THRIFT_CLIENT_CLASS = TestService.Client
    static String SERVICE_ID = THRIFT_CLIENT_CLASS.canonicalName
    static String SERVICE_NAME = 'TestService'
    static String OPERATION_NAME = 'testOperation'
    static String THRIFT_SIMPLE_FIELD = 'someSimpleField'

    static List<TService> services() {
        [new TService(AnotherTestService.Client), new TService(SubTestService.Client), service()]
    }

    static TService service() {
        new TService(THRIFT_CLIENT_CLASS)
    }

    static TOperation operation() {
        service().operation(OPERATION_NAME)
    }

    static TestStruct thriftTestStruct() {
        new TestStruct().tap {
            stringField = 'some'
            boolField = true
            byteField = 1 as byte
            i16Field = 2 as short
            i32Field = 3
            i64Field = 4
            doubleField = 5.5d
            enumField = TestEnum.ENUM_2
            binaryField = [1, 2, 3] as byte[]
            innerComplexField = thriftTestInnerStruct()
            listInnerComplexField = [thriftTestInnerStruct()]
        }
    }

    static TestInnerStruct thriftTestInnerStruct() {
        new TestInnerStruct('f1', 'f2')
    }

    static TestException thriftException() {
        new TestException([
                new ErrorInfo().tap {
                    code = 'errorCode'
                    message = 'errorMessage'
                }
        ])
    }

    static ProxyRequestDTO proxyRequest() {
        new ProxyRequestDTO().tap {
            endpoint = THRIFT_ENDPOINT
            body = proxyRequestBody()
        }
    }

    static JsonNode proxyRequestBody() {
        def node = JsonUtils.objectNode()
        node.put('simpleField', THRIFT_SIMPLE_FIELD)
        node.set('complexField', jsonTestStruct())
        node.set('listComplexField', JsonUtils.arrayNode().add(jsonTestInnerStruct()))
        node.set('setComplexField', JsonUtils.arrayNode().add(jsonTestInnerStruct()))
        return node
    }

    static Object[] proxyArgs() {
        new Object[]{THRIFT_SIMPLE_FIELD, thriftTestStruct(), [thriftTestInnerStruct()], [thriftTestInnerStruct()].toSet()}
    }

    static JsonNode jsonTestStruct() {
        def thrift = thriftTestStruct()
        def node = JsonUtils.objectNode()
        node.put('stringField', thrift.stringField)
        node.put('boolField', thrift.boolField)
        node.put('byteField', thrift.byteField)
        node.put('i16Field', thrift.i16Field)
        node.put('i32Field', thrift.i32Field)
        node.put('i64Field', thrift.i64Field)
        node.put('doubleField', thrift.doubleField)
        node.put('enumField', thrift.enumField.name())
        node.put('binaryField', thrift.binaryField)
        node.set('innerComplexField', jsonTestInnerStruct())
        node.set('listInnerComplexField', JsonUtils.arrayNode().add(jsonTestInnerStruct()))
        return node
    }

    static JsonNode jsonTestInnerStruct() {
        def thrift = thriftTestInnerStruct()
        JsonUtils.objectNode()
                .put('f1', thrift.f1)
                .put('f2', thrift.f2)
    }

    static SpecType proxyRequestSpecType() {
        SpecType.object(
                new SpecField('simpleField', SpecType.primitive(DataType.STRING)),
                new SpecField('complexField', SpecType.object(
                        new SpecField('stringField', SpecType.primitive(DataType.STRING)),
                        new SpecField('boolField', SpecType.primitive(DataType.BOOLEAN)),
                        new SpecField('byteField', SpecType.primitive(DataType.NUMBER)),
                        new SpecField('i16Field', SpecType.primitive(DataType.NUMBER)),
                        new SpecField('i32Field', SpecType.primitive(DataType.NUMBER)),
                        new SpecField('i64Field', SpecType.primitive(DataType.NUMBER)),
                        new SpecField('doubleField', SpecType.primitive(DataType.NUMBER)),
                        new SpecField('enumField', SpecType.primitive(DataType.STRING)),
                        new SpecField('binaryField', SpecType.primitive(DataType.STRING)),
                        new SpecField('innerComplexField', innerComplexSpecType()),
                        new SpecField('listInnerComplexField', SpecType.array(innerComplexSpecType()))
                )),
                new SpecField('listComplexField', SpecType.array(innerComplexSpecType())),
                new SpecField('setComplexField', SpecType.array(innerComplexSpecType()))
        )
    }

    static SpecType innerComplexSpecType() {
        SpecType.object(
                new SpecField('f1', SpecType.primitive(DataType.STRING)),
                new SpecField('f2', SpecType.primitive(DataType.STRING))
        )
    }

    static String templateSpec() {
        """
        {
          "simpleField": "",
          "complexField": {
            "stringField": "",
            "boolField": false,
            "byteField": 0,
            "i16Field": 0,
            "i32Field": 0,
            "i64Field": 0,
            "doubleField": 0,
            "enumField": "",
            "binaryField": "",
            "innerComplexField": {
              "f1": "",
              "f2": ""
            },
            "listInnerComplexField": [{
              "f1": "",
              "f2": ""
            }]
          },
          "listComplexField": [{
            "f1": "",
            "f2": ""
          }],
          "setComplexField": [{
            "f1": "",
            "f2": ""
          }]
        }"""
    }
}
