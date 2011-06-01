
package com.google.code.simplejstest;

import java.util.List;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

/**
 *
 * @author nick
 */
public class SimpleJsTest {
    private static final SimpleJsTestResult PASS_RESULT =
                        new SimpleJsTestResult(SimpleJsTestResult.State.PASS, null);

    private final String name;
    private final String testFileContent;
    private final List<SimpleJsTestDependency> dependencies;
    private final ScriptEngine scriptEngine;

    SimpleJsTest(String name,
                 String testFileContent,
                 List<SimpleJsTestDependency> dependencies,
                 ScriptEngine scriptEngine) {
        this.name = name;
        this.testFileContent = testFileContent;
        this.dependencies = dependencies;
        this.scriptEngine = scriptEngine;
    }

    /**
     * Executes the test and returns a pass or fail result.
     *
     * @return the test result.
     *
     * @throws SimpleJsTestSystemException if an unexpected error occurs.
     */
    public SimpleJsTestResult execute() {
        StringBuilder script = new StringBuilder();
        for (SimpleJsTestDependency dependency : dependencies) {
			script.append(dependency.getContent());
            script.append("\n");
        }
        script.append(testFileContent);
        script.append("\n");
        script.append(Utils.loadInternalFile("/javascript/assertions.js"));
        script.append("\n");
        script.append(Utils.loadInternalFile("/javascript/test_runner.js"));
        script.append("\n");

        String invocation = "tests.runTest('" + this.name + "');";
        script.append(invocation);

        Bindings bindings = scriptEngine.createBindings();

        Object result = null;
        try {
            result = scriptEngine.eval(script.toString(), bindings);
        } catch (ScriptException se) {
            return new SimpleJsTestResult(SimpleJsTestResult.State.FAIL,
                                          "Test script failed: " + se.getMessage());
        }

        if (result==null) {
            throw new SimpleJsTestSystemException("Test returned no result");
        }

        if (((result instanceof Boolean) && ((Boolean)result) == true) || "mvn-js-test-[pass]".equals(result.toString())) {
            return PASS_RESULT;
        }
        
        return new SimpleJsTestResult(SimpleJsTestResult.State.FAIL,
                                      "Test script failed: " + result);
    }

    /**
     * Returns the name of the test.
     *
     * @return the name specified in the file.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the dependencies specified in the test file.
     *
     * @return the dependencies.
     */
    public List<SimpleJsTestDependency> getDependencies() {
        return dependencies;
    }

}
