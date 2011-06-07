
package com.google.code.simplejstest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Main entry point to the framework.
 *
 * @author nick
 */
public class SimpleJsTestLoader {

    private final TestResourceLoader resourceLoader;
    private final ScriptEngine scriptEngine;


    /**
     * Creates a loader with a resource loader that will be used to load dependencies specified
     * in the test file.
     * 
     * @param resourceLoader the loader to use to resolve test dependencies.
     */
    public SimpleJsTestLoader(TestResourceLoader resourceLoader) {
        if (resourceLoader==null) {
            throw new NullPointerException("A resource loader must be specified");
        }

        this.resourceLoader = resourceLoader;

        ScriptEngineManager manager = new ScriptEngineManager();
        scriptEngine = manager.getEngineByName("JavaScript");
    }

    /**
     * Loads the tests described in the passed file.
     *
     * @param fileContents the contents of the test file.
     *
     * @return the tests, ready to be executed.
     *
     * @throws FileNotFoundException if thrown when loading declared dependencies.
     * @throws IOException if thrown when loading declared dependencies.
     */
    public List<SimpleJsTest> loadTestsForFile(String fileContents)
            throws FileNotFoundException,
                   IOException {
        List<SimpleJsTest> tests = new ArrayList<SimpleJsTest>();

        List<String> testNames = getTestNamesFromFile(fileContents);
        for (String testName : testNames) {

            List<String> testDependencyNames = getTestDependenciesFromFile(fileContents);

            List<SimpleJsTestDependency> testDependencies = new ArrayList<SimpleJsTestDependency>();
            for (String dependencyName : testDependencyNames) {
                testDependencies.add(
                        new SimpleJsTestDependency(dependencyName,
                                                   resourceLoader.loadResource(dependencyName)));
            }

            tests.add(new SimpleJsTest(testName, fileContents, testDependencies, scriptEngine));
        }

        return tests;
    }


    private List<String> getTestNamesFromFile(String fileContents) {

        StringBuilder script = new StringBuilder(fileContents);

        script.append("\n");

        script.append(Utils.loadInternalFile("/javascript/list_tests.js"));

        Bindings bindings = scriptEngine.createBindings();
        String result = null;
        try {
            result = (String) scriptEngine.eval(script.toString(), bindings);
        } catch (ScriptException se) {
            throw new SimpleJsTestSystemException("Could not retieve test names from file or JavaScript syntax error: "+se.getMessage(), se);
        }

        return Arrays.asList(result.split(","));
    }
    

    private List<String> getTestDependenciesFromFile(String fileContents) {

        StringBuilder script = new StringBuilder(fileContents);

        script.append("\n");

        script.append(Utils.loadInternalFile("/javascript/list_dependencies.js"));

        Bindings bindings = scriptEngine.createBindings();
        String result = null;
        try {
            result = (String) scriptEngine.eval(script.toString(), bindings);
        } catch (ScriptException se) {
            throw new SimpleJsTestSystemException("Could not retieve test dependencies from file", se);
        }

        return result.trim().isEmpty() ? Collections.EMPTY_LIST :
                                         Arrays.asList(result.split(","));
    }

}
