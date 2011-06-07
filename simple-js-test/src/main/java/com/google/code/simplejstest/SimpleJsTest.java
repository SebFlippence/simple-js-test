
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
    private final String name;
    private final String testFileContent;
    private final List<SimpleJsTestDependency> dependencies;
    private final ScriptEngine scriptEngine;
    private long testStartTime;
    private float testExeTime;

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
        testStartTime = System.currentTimeMillis();
        try {            
            result = scriptEngine.eval(script.toString(), bindings);
        } catch (ScriptException se) {
            return new SimpleJsTestResult(this.getName(), SimpleJsTestResult.State.FAIL,
                                          "Test script failed: " + se.getMessage(), new Float(String.format("%.3f", (System.currentTimeMillis() - testStartTime) / 1000.)));
        }
        testExeTime = new Float(String.format("%.3f", (System.currentTimeMillis() - testStartTime) / 1000.));

        if (result==null) {
            throw new SimpleJsTestSystemException("Test returned no result");
        }

        if (((result instanceof Boolean) && ((Boolean)result) == true) || "mvn-js-test-[pass]".equals(result.toString())) {
            return new SimpleJsTestResult(this.getName(), SimpleJsTestResult.State.PASS, null, testExeTime);
        }
        
        return new SimpleJsTestResult(this.getName(), SimpleJsTestResult.State.FAIL,
                                      "Test script failed: " + result, testExeTime);
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
