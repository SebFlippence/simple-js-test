
function assertFail(msg) {
    throw "Assertion failed" + ((msg !== undefined) ? ": " + msg : "");
}

function assertPass() {
    throw "mvn-js-test-[pass]";
}

function assertEquals(lhs, rhs, msg) {
    if (lhs != rhs) {
        throw "Assertion failed: '" + lhs + "' != '" + rhs + "'" + ((msg !== undefined) ? ": " + msg : "");
    }
}

function assertNotEquals(lhs, rhs, msg) {
    if (lhs == rhs) {
        throw "Assertion failed: '" + lhs + "' == '" + rhs + "'" + ((msg !== undefined) ? ": " + msg : "");
    }
}

function assertTrue(value, msg) {
    if (!value) {
        throw "Assertion failed: '" + value + "' is not true" + ((msg !== undefined) ? ": " + msg : "");
    }
}

function assertFalse(value, msg) {
    if (value) {
        throw "Assertion failed: '" + value + "' is not false" + ((msg !== undefined) ? ": " + msg : "");
    }
}
