namespace java io.github.artemy.osipov.thrift.bridge.test

struct TestStruct {
    1: string stringField
    2: bool boolField
    3: byte byteField
    4: i16 i16Field
    5: i32 i32Field
    6: i64 i64Field
    7: double doubleField
    8: TestEnum enumField
    9: binary binaryField
    10: TestInnerStruct innerComplexField
    11: list<TestInnerStruct> listInnerComplexField
}

struct TestInnerStruct {
    1: string f1
    2: string f2
}

enum TestEnum {
    ENUM_1
    ENUM_2
}

exception TestException {
    1: list<ErrorInfo> errors
}

struct ErrorInfo {
    1: string code
    2: string message
}

service TestService {

    list<TestStruct> testOperation (
        1: string simpleField
        2: TestStruct complexField
        3: list<TestInnerStruct> listComplexField
        4: set<TestInnerStruct> setComplexField
    ) throws (1: TestException e)
}

service SubTestService extends TestService {

    void subOperation (
        1: string simpleField
    )
}

service AnotherTestService {

    void voidOperation (
        1: string simpleField
    )
}
