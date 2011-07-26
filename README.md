simple-js-test
---------------
### Simple JavaScript Unit Testing 

This is a fork of [nickbfaulkner's](http://code.google.com/u/@UBRfQFVZBxJBXwJ%2F/) [simple-js-test from Google Code](http://code.google.com/p/simple-js-test/).

#### simple-js-test provides

- Instance-per-test (a la JUnit) behavior. No need to tidy up your JS state after each unit test as each test function is executed with a clean slate.
- Easy configuration of the JavaScript code to be included for the tests. E.g. test one or all script files at a time.
- Simple integration into Maven projects. 

#### What this fork adds

- Changed behaviour of failing tests, so that all tests are run (even if they fail) and a summary of failures is shown
- Added JUnit style reports (which can display reports & graphs in Hudson/Jenkins etc.)
- Better error handling
- If a test fails; the evaluated test case is output to the report folder (to allow debugging)
- Added optional fail messages to assertions
- Added count of passing and failing unit tests
- Only executes simple-js-test files that end in .js (to avoid system meta data files)

#### Example test file

JavaScript:

    var dependencies = ["/main/webapp/js/target_file.js",
                    "/test/javascript/common_test_utils.js"];

    var setUp = function() {
        println("running setup");
    }

    var tests = {
        testA : function() {
            assertTrue(true);
        },
        testB : function() {
            assertEquals(1, 1);
        }
    };

#### See also:

- [Maven2Integration](http://code.google.com/p/simple-js-test/wiki/Maven2Integration)
- [AssertionsReference](http://code.google.com/p/simple-js-test/wiki/AssertionsReference)
- [nickbfaulkner on Google Code](http://code.google.com/u/@UBRfQFVZBxJBXwJ%2F/)
- [The original simple-js-test from Google Code](http://code.google.com/p/simple-js-test/).

Code license: [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0)
