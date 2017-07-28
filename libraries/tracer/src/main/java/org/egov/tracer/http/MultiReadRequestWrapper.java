package org.egov.tracer.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
/*
    To read the request body multiple times the implementation is taken from the below mentioned URL.
    http://stackoverflow.com/questions/10210645/http-servlet-request-lose-params-from-post-body-after-read-it-once
 */
@Slf4j
public class MultiReadRequestWrapper extends HttpServletRequestWrapper {

    private ByteArrayOutputStream cachedBytes;

    public MultiReadRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (cachedBytes == null)
            cacheInputStream();

        return new CachedServletInputStream();
    }

    public void update(ByteArrayOutputStream newBytes) {
        this.cachedBytes = newBytes;
    }

    @Override
    public int getContentLength() {
        return this.cachedBytes.size();
    }

    @Override
    public long getContentLengthLong() {
        return this.cachedBytes.size();
    }

    @Override
    public BufferedReader getReader() throws IOException{
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    private void cacheInputStream() throws IOException {
        cachedBytes = new ByteArrayOutputStream();
        IOUtils.copy(super.getInputStream(), cachedBytes);
    }

    public class CachedServletInputStream extends ServletInputStream {
        private ByteArrayInputStream input;

        public CachedServletInputStream() {
            input = new ByteArrayInputStream(cachedBytes.toByteArray());
        }

        @Override
        public boolean isFinished() {
            return input.available() != 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {

        }

        @Override
        public int read() throws IOException {
            return input.read();
        }
    }
}


