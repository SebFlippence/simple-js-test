
tests.runTest = function(testName) {
    if (setUp) {
        setUp();
    }
    try {
       tests[testName]();
    } catch (exception) {
        //println(exception);
        return exception;
    }
    return true;
};

