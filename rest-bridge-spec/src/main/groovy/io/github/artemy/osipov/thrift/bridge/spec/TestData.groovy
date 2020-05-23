package io.github.artemy.osipov.thrift.bridge.spec

import groovy.transform.CompileStatic
import io.github.artemy.osipov.thrift.bridge.core.TService
import io.github.artemy.osipov.thrift.bridge.core.TService.TOperation
import io.github.artemy.osipov.thrift.bridge.test.TestService
import io.github.artemy.osipov.thrift.bridge.test.TestStruct
import org.apache.thrift.TServiceClient

@CompileStatic
class TestData {

    static String THRIFT_ENDPOINT = 'http://some.com/tapi'
    static Class<? extends TServiceClient> THRIFT_CLIENT_CLASS = TestService.Client
    static String SERVICE_ID = THRIFT_CLIENT_CLASS.canonicalName
    static String SERVICE_NAME = 'TestService'
    static String OPERATION_NAME = 'testOperation'

    static TService service() {
        new TService(THRIFT_CLIENT_CLASS)
    }

    static TOperation operation() {
        service().operation(OPERATION_NAME)
    }

    static TestStruct thriftTestStruct() {
        new TestStruct().tap {
            stringField = 'some'
        }
    }

    static String proxyRequest() {
        """
          {
            "endpoint": "$THRIFT_ENDPOINT",
            "body": ${proxyRequestBody()}
          }
        """
    }

    static String proxyRequestBody() {
        """{"field": "value"}"""
    }

    static String templateSpec() {
        """
        {
          "simpleField": "",
          "complexField": {
            "stringField": "",
            "enumField": ""
          }
        }"""
    }
}
