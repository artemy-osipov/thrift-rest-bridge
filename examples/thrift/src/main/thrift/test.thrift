namespace java io.github.artemy.osipov.thrift.bridge.test

struct TestComplexStruct {
    1: string stringField
    2: bool boolField
    3: byte byteField
    4: i16 i16Field
    5: i32 i32Field
    6: i64 i64Field
    7: double doubleField
    8: TestEnum enumField
    9: binary binaryField
    10: TestSimpleStruct structField
    11: list<TestSimpleStruct> listStructField
    12: TestUnion unionField
}

struct TestSimpleStruct {
    1: required bool f1
    2: required string f2
}

struct TestRecursiveStruct {
    1: string f1
    2: TestRecursiveStruct recursive
}

enum TestEnum {
    ENUM_1
    ENUM_2
}

union TestUnion {
    1: TestEnum enum1
    2: TestEnum enum2
}

union TestComplexUnion {
    1: TestEnum enumField
    2: TestSimpleStruct structField
}

exception TestException {
    1: list<ErrorInfo> errors
}

struct ErrorInfo {
    1: string code
    2: string message
}

service TestService {

    list<TestComplexStruct> testOperation (
        1: string simpleField
        2: TestComplexStruct complexField
        3: list<TestSimpleStruct> listStructField
        4: set<TestSimpleStruct> setStructField
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
