package org.egov.egf.budget.utils;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

public class RequestJsonReader {

    public String readRequest(final String fileName) {
        try {
            final String info = IOUtils.toString(
                    this.getClass().getClassLoader().getResourceAsStream("common/request_info.json"), "UTF-8");

            final String data = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
            return "{\n" + info + "," + data + "}";

        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String readResponse(final String fileName) {
        try {
            final String info = IOUtils.toString(
                    this.getClass().getClassLoader().getResourceAsStream("common/response_info.json"), "UTF-8");

            final String data = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
            return "{\n" + info + "," + data + "}";

        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String readErrorResponse(final String fileName) {
        try {
            final String info = IOUtils
                    .toString(this.getClass().getClassLoader().getResourceAsStream("common/error_info.json"), "UTF-8");

            final String data = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(fileName), "UTF-8");
            return "{\n" + info + "," + data + "}";

        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getRequestInfo() {
        try {
            final String info = IOUtils.toString(
                    this.getClass().getClassLoader().getResourceAsStream("common/request_info.json"), "UTF-8");

            return "{\n" + info + "}";

        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getFileContents(final String fileName) {
        try {
            return IOUtils.toString(this.getClass().getClassLoader()
                    .getResourceAsStream(fileName), "UTF-8");
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}