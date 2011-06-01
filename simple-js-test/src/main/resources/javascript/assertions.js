
function assertFail() {
    throw "Assertion failed";
}

function assertPass() {
    throw "mvn-js-test-[pass]";
}

function assertEquals(lhs, rhs) {
    if (lhs != rhs) {
        throw "Assertion failed: '" + lhs + "' != '" + rhs + "'";
    }
}

function assertNotEquals(lhs, rhs) {
    if (lhs == rhs) {
        throw "Assertion failed: '" + lhs + "' == '" + rhs + "'";
    }
}

function assertTrue(value) {
    if (!value) {
        throw "Assertion failed: '" + value + "' is not true";
    }
}

function assertFalse(value) {
    if (value) {
        throw "Assertion failed: '" + value + "' is not false";
    }
}
