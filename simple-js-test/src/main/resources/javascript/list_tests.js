var testVarConverterResponse = null;
for (test in tests) {
    testVarConverterResponse = testVarConverterResponse == null ? 
                                                test :
                                                testVarConverterResponse + "," + test;
}
