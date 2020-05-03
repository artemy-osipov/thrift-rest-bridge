package io.github.artemy.osipov.thrift.bridge

import com.fasterxml.jackson.databind.JsonNode
import groovy.transform.CompileStatic
import io.github.artemy.osipov.thrift.bridge.controllers.dto.ProxyRequestDTO
import io.github.artemy.osipov.thrift.bridge.core.TService
import io.github.artemy.osipov.thrift.bridge.core.TService.TOperation
import org.apache.thrift.TServiceClient
import io.github.artemy.osipov.thrift.bridge.test.AnotherTestService
import io.github.artemy.osipov.thrift.bridge.test.ErrorInfo
import io.github.artemy.osipov.thrift.bridge.test.SubTestService
import io.github.artemy.osipov.thrift.bridge.test.TestEnum
import io.github.artemy.osipov.thrift.bridge.test.TestException
import io.github.artemy.osipov.thrift.bridge.test.TestInnerStruct
import io.github.artemy.osipov.thrift.bridge.test.TestService
import io.github.artemy.osipov.thrift.bridge.test.TestStruct

import static io.github.artemy.osipov.thrift.bridge.utils.JsonUtils.objectNode

@CompileStatic
class TestData {

    static String THRIFT_ENDPOINT = 'http://some.com/tapi'
    static String SERVICE_NAME = 'TestService'
    static String OPERATION_NAME = 'testOperation'
    static String THRIFT_SIMPLE_FIELD = 'someSimpleField'
    static Class<? extends TServiceClient> THRIFT_CLIENT_CLASS = TestService.Client

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
            intField = 42
            enumField = TestEnum.ENUM_2
            binaryField = [1, 2, 3] as byte[]
            complexField = new TestInnerStruct('f1', 'f2')
        }
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
        objectNode()
                .put('simpleField', THRIFT_SIMPLE_FIELD)
                .set('complexField', jsonTestStruct())
    }

    static Object[] proxyArgs() {
        new Object[]{THRIFT_SIMPLE_FIELD, thriftTestStruct()}
    }

    static JsonNode jsonTestStruct() {
        def thrift = thriftTestStruct()
        objectNode()
                .put('stringField', thrift.stringField)
                .put('boolField', thrift.boolField)
                .put('intField', thrift.intField)
                .put('enumField', thrift.enumField.name())
                .put('binaryField', thrift.binaryField)
                .set('complexField',
                        objectNode()
                                .put('f1', thrift.complexField.f1)
                                .put('f2', thrift.complexField.f2)
                )
    }
}
