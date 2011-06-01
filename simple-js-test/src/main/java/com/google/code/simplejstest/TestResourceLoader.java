
package com.google.code.simplejstest;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 
 *
 * @author nick
 */
public interface TestResourceLoader {

    /**
     * Loads the resource identified by the path specified.
     * 
     * @param path the path specified in the dependency section of the test file
     * 
     * @return the contents of the file.
     *
     * @throws FileNotFoundException if the resource does not exist.
     * @throws IOException if there is a problem loading the resource.
     */
    String loadResource(String path) throws FileNotFoundException,
                                            IOException;
}
