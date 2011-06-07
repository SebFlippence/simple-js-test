
package com.google.code.simplejstest;

/**
 *
 * @author nick
 */
public class SimpleJsTestResult {
    public enum State {
        PASS,
        FAIL;
    }

    private final String name;
    private final State state;
    private final String message;
    private final float time;

    SimpleJsTestResult(String name, State state, String message, float time) {
        this.name = name;
        this.state = state;
        this.message = message;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public State getState() {
        return state;
    }

    public String getMessage() {        
        return message;
    }

    public float getTime() {
        return time;
    }

}
