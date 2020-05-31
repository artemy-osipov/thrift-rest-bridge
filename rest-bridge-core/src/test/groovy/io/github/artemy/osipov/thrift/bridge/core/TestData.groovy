package io.github.artemy.osipov.thrift.bridge.core

import groovy.transform.CompileStatic
import io.github.artemy.osipov.thrift.bridge.core.spec.DataType
import io.github.artemy.osipov.thrift.bridge.core.spec.SpecField
import io.github.artemy.osipov.thrift.bridge.core.spec.SpecType
import io.github.artemy.osipov.thrift.bridge.test.TestComplexStruct
import io.github.artemy.osipov.thrift.bridge.test.TestService
import io.github.artemy.osipov.thrift.bridge.test.TestUnion
import org.apache.thrift.TServiceClient
import io.github.artemy.osipov.thrift.bridge.test.AnotherTestService
import io.github.artemy.osipov.thrift.bridge.test.ErrorInfo
import io.github.artemy.osipov.thrift.bridge.test.SubTestService
import io.github.artemy.osipov.thrift.bridge.test.TestEnum
import io.github.artemy.osipov.thrift.bridge.test.TestException
import io.github.artemy.osipov.thrift.bridge.test.TestSimpleStruct

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

    static TService.TOperation operation() {
        service().operation(OPERATION_NAME)
    }

    static TestComplexStruct thriftComplexStruct() {
        new TestComplexStruct().tap {
            stringField = 'some'
            boolField = true
            byteField = 1 as byte
            i16Field = 2 as short
            i32Field = 3
            i64Field = 4
            doubleField = 5.5d
            enumField = TestEnum.ENUM_2
            binaryField = [1, 2, 3] as byte[]
            structField = thriftSimpleStruct()
            listStructField = [thriftSimpleStruct()]
            unionField = new TestUnion().tap {
                enum1 = TestEnum.ENUM_1
            }
        }
    }

    static TestSimpleStruct thriftSimpleStruct() {
        new TestSimpleStruct(true, 'f2')
    }

    static TestException thriftException() {
        new TestException([
                new ErrorInfo().tap {
                    code = 'errorCode'
                    message = 'errorMessage'
                }
        ])
    }

    static Object[] proxyArgs() {
        new Object[]{THRIFT_SIMPLE_FIELD, thriftComplexStruct(), [thriftSimpleStruct()], [thriftSimpleStruct()].toSet()}
    }

    static String proxyRequestBody() {
        """
          {
            "simpleField": "$THRIFT_SIMPLE_FIELD",
            "complexField": ${jsonComplexStruct()},
            "listStructField": [${jsonSimpleStruct()}],
            "setStructField": [${jsonSimpleStruct()}]
          }
        """
    }

    static String jsonComplexStruct() {
        def thrift = thriftComplexStruct()
        """
          {
            "stringField": "$thrift.stringField",
            "boolField": $thrift.boolField,
            "byteField": $thrift.byteField,
            "i16Field": $thrift.i16Field,
            "i32Field": $thrift.i32Field,
            "i64Field": $thrift.i64Field,
            "doubleField": $thrift.doubleField,
            "enumField": "$thrift.enumField",
            "binaryField": "${thrift.binaryField.encodeBase64()}",
            "structField": ${jsonSimpleStruct()},
            "listStructField": [${jsonSimpleStruct()}],
            "unionField": { "enum1": "$thrift.unionField.enum1" }
          }
        """
    }

    static String jsonSimpleStruct() {
        def thrift = thriftSimpleStruct()
        """
          {
            "f1": $thrift.f1,
            "f2": "$thrift.f2"
          }
        """
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
                        new SpecField('structField', complexSpecType()),
                        new SpecField('listStructField', SpecType.array(complexSpecType())),
                        new SpecField('unionField', SpecType.object())
                )),
                new SpecField('listStructField', SpecType.array(complexSpecType())),
                new SpecField('setStructField', SpecType.array(complexSpecType()))
        )
    }

    static SpecType complexSpecType() {
        SpecType.object(
                new SpecField('f1', SpecType.primitive(DataType.BOOLEAN)),
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
            "structField": {
              "f1": false,
              "f2": ""
            },
            "listStructField": [{
              "f1": false,
              "f2": ""
            }],
            "unionField": {
            }
          },
          "listStructField": [{
            "f1": false,
            "f2": ""
          }],
          "setStructField": [{
            "f1": false,
            "f2": ""
          }]
        }"""
    }
}
