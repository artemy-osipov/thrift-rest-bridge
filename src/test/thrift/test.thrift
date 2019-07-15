namespace java ru.osipov.thrift.bridge.test

struct Test {
    1: string stringField
    2: bool boolField
    3: i32 intField
    4: TestEnum enumField
    5: TestComplex complexField
}

struct TestComplex {
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

    list<Test> testOperation (
        1: string simpleField
        2: Test complexField
    ) throws (1: TestException e)
}

service AnotherTestService {

    void voidOperation (
        1: string simpleField
    )
}
