package ru.osipov.thrift.bridge

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.transform.CompileStatic
import org.apache.thrift.TServiceClient
import ru.osipov.thrift.bridge.domain.TOperation
import ru.osipov.thrift.bridge.domain.TService
import ru.osipov.thrift.bridge.test.AnotherTestService
import ru.osipov.thrift.bridge.test.ErrorInfo
import ru.osipov.thrift.bridge.test.SubTestService
import ru.osipov.thrift.bridge.test.TestEnum
import ru.osipov.thrift.bridge.test.TestException
import ru.osipov.thrift.bridge.test.TestInnerStruct
import ru.osipov.thrift.bridge.test.TestService
import ru.osipov.thrift.bridge.test.TestStruct

@CompileStatic
class TestData {

    static String THRIFT_ENDPOINT = 'http://some.com/tapi'
    static String SERVICE_NAME = 'TestService'
    static String OPERATION_NAME = 'testOperation'
    static String THRIFT_SIMPLE_FIELD = 'someSimpleField'
    static String THRIFT_PACKAGE = 'ru.osipov.thrift.bridge.test'
    static Class<? extends TServiceClient> THRIFT_CLIENT_CLASS = TestService.Client

    private static ObjectMapper mapper = new ObjectMapper()

    static List<TService> services() {
        [TService.build(AnotherTestService.Client), TService.build(SubTestService.Client), service()]
    }

    static TService service() {
        TService.build(THRIFT_CLIENT_CLASS)
    }

    static TOperation operation() {
        service().getOperation(OPERATION_NAME)
    }

    static TestStruct thriftTestStruct() {
        new TestStruct().tap {
            stringField = 'some'
            boolField = true
            intField = 42
            enumField = TestEnum.ENUM_2
            complexField = new TestInnerStruct('f1', 'f2')
        }
    }

    static List<TestStruct> thriftResponse() {
        [thriftTestStruct(), thriftTestStruct()]
    }

    static TestException thriftException() {
        new TestException([
                new ErrorInfo().tap {
                    code = 'errorCode'
                    message = 'errorMessage'
                }
        ])
    }

    static JsonNode restRequest() {
        def thrift = thriftTestStruct()
        mapper.createObjectNode()
                .put('simpleField', THRIFT_SIMPLE_FIELD)
                .set('complexField',
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
                )
    }

    static JsonNode restTest() {
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

    static JsonNode restResponse() {
        mapper.createArrayNode()
                .add(restTest())
                .add(restTest())
    }

    static JsonNode restException() {
        def thrift = thriftException()
        mapper.createObjectNode()
                .set('errors', mapper.createArrayNode().add(
                        mapper.createObjectNode()
                                .put('code', thrift.errors[0].code)
                                .put('message', thrift.errors[0].message)
                ))
    }
}
