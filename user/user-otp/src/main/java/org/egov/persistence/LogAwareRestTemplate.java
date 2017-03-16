package org.egov.persistence;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;

@Slf4j
public class LogAwareRestTemplate extends RestTemplate {

    public LogAwareRestTemplate() {
        this.setInterceptors(Collections.singletonList(logRequestAndResponse()));
    }

    private ClientHttpRequestInterceptor logRequestAndResponse() {
        return (httpRequest, body, clientHttpRequestExecution) -> {
            logRequest(httpRequest, body);
            final ClientHttpResponse response = clientHttpRequestExecution.execute(httpRequest, body);
            logResponse(response);
            return response;
        };
    }

    private void logResponse(ClientHttpResponse response) {
        if(!log.isInfoEnabled()) {
            return;
        }
        String body = getBodyString(response);
        try {
            log.info("Received response code {} and body {}: ", response.getStatusCode(), body);
        } catch (IOException e) {
           throw new RuntimeException(e);
        }
    }

    private String getBodyString(ClientHttpResponse response) {
        try {
            if (response != null && response.getBody() != null) {
                return IOUtils.toString(response.getBody(), "UTF-8");
            } else {
                return "<EMPTY>";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void logRequest(HttpRequest httpRequest, byte[] body) {
        if (!log.isInfoEnabled()) {
            return;
        }
        log.info("Sending request to {} with verb {} with body {}",
                httpRequest.getURI(),
                httpRequest .getMethod().name(), getBody(body));
    }

    private String getBody(byte[] body) {
        return body == null ? "<EMPTY>" : new String(body);
    }
}
