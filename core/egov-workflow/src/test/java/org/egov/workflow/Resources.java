package org.egov.workflow;

import org.apache.commons.io.IOUtils;

import java.io.IOException;

public class Resources {

    private static final String UTF_8 = "UTF-8";

    public String getFileContents(final String fileName) {
        try {
            return IOUtils.toString(this.getClass().getClassLoader()
                .getResourceAsStream(fileName), UTF_8).replace("\n", "");
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
