
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

    private final State state;
    private final String message;

    SimpleJsTestResult(State state, String message) {
        this.state = state;
        this.message = message;
    }

    public State getState() {
        return state;
    }

    public String getMessage() {        
        return message;
    }

}
