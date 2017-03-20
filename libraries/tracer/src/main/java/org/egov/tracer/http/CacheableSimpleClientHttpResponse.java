package org.egov.tracer.http;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.AbstractClientHttpResponse;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class CacheableSimpleClientHttpResponse extends AbstractClientHttpResponse {

    private ClientHttpResponse clientHttpResponse;
    private String cachedInput;

    public CacheableSimpleClientHttpResponse(ClientHttpResponse clientHttpResponse) {
        this.clientHttpResponse = clientHttpResponse;
    }

    @Override
    public int getRawStatusCode() throws IOException {
        return clientHttpResponse.getRawStatusCode();
    }

    @Override
    public String getStatusText() throws IOException {
        return clientHttpResponse.getStatusText();
    }

    @Override
    public void close() {
        clientHttpResponse.close();
    }

    @Override
    public InputStream getBody() throws IOException {
        if (cachedInput != null) {
            return new ByteArrayInputStream(cachedInput.getBytes(StandardCharsets.UTF_8));
        }
        cachedInput = new String(IOUtils.toByteArray(clientHttpResponse.getBody()));
        return new ByteArrayInputStream(cachedInput.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public HttpHeaders getHeaders() {
        return clientHttpResponse.getHeaders();
    }
}
