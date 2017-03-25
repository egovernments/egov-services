package org.egov.tracer.http;

import com.netflix.zuul.http.HttpServletRequestWrapper;
import com.netflix.zuul.http.ServletInputStreamWrapper;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class MultiReadableRequestWrapper extends HttpServletRequestWrapper {

    private static final String UTF_8 = "UTF-8";
    private String payload;

    public MultiReadableRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        payload = IOUtils.toString(request.getInputStream(), UTF_8);
    }

    public String getPayload() {
        return payload;
    }

    @Override
    public ServletInputStream getInputStream() {
        return new ServletInputStreamWrapper(payload.getBytes());
    }

}


