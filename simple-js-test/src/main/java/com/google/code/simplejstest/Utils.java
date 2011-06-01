
package com.google.code.simplejstest;

import com.google.common.io.ByteStreams;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author nick
 */
class Utils {

    static String loadInternalFile(String filePath) {
        try {
            InputStream fileStream = Utils.class.getResourceAsStream(filePath);
            if (fileStream == null) {
                throw new FileNotFoundException(
                                            "File does not exist: " + filePath);
            }
            return new String(ByteStreams.toByteArray(fileStream));
        } catch (IOException ioe) {
            throw new SimpleJsTestSystemException(
                            "Could not load test resource: " + filePath, ioe);
        }
    }
}
