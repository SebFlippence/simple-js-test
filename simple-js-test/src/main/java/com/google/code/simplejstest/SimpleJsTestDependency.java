
package com.google.code.simplejstest;

/**
 * Represents a dependency specified in the test file.
 * 
 * @author nick
 */
public class SimpleJsTestDependency {

    private final String name;
    private final String content;

    SimpleJsTestDependency(String name, String content) {
        this.name = name;
        this.content = content;
    }

    /**
     * the name of the dependency.
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }


    /**
     * the content of the file.
     *
     * @return the content.
     */
    public String getContent() {
        return content;
    }


}
