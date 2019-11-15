namespace java io.github.artemy.osipov.thrift.bridge.test

struct TestStruct {
    1: string stringField
    2: bool boolField
    3: i32 intField
    4: TestEnum enumField
    5: binary binaryField
    6: TestInnerStruct complexField
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
