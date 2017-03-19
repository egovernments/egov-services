package org.egov.tracer.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.egov.tracer.model.RequestContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component("logAwareRestTemplate")
public class LogAwareRestTemplate extends RestTemplate {

    private static final String EMPTY_MESSAGE = "<EMPTY>";
    private static final String REQUEST_MESSAGE = "Sending request to {} with verb {} with body {}";
    private static final String RESPONSE_MESSAGE = "Received response code {} and body {}: ";
    private static final String UTF_8 = "UTF-8";
    private static final String RESPONSE_ERROR_MESSAGE = "Error reading response";

    public LogAwareRestTemplate() {
        final List<ClientHttpRequestInterceptor> interceptors =
                Arrays.asList(correlationIdInterceptor(), loggingInterceptor());
        this.setInterceptors(interceptors);
    }

    private ClientHttpRequestInterceptor correlationIdInterceptor() {
        return (httpRequest, bytes, clientHttpRequestExecution) -> {
            final HttpHeaders headers = httpRequest.getHeaders();
            headers.put(RequestContext.CORRELATION_ID, Collections.singletonList(RequestContext.getId()));

            return clientHttpRequestExecution.execute(httpRequest, bytes);
        };
    }

    private ClientHttpRequestInterceptor loggingInterceptor() {
        return (httpRequest, body, clientHttpRequestExecution) -> {
            logRequest(httpRequest, body);
            final ClientHttpResponse response = clientHttpRequestExecution.execute(httpRequest, body);
            logResponse(response);
            return response;
        };
    }

    private void logResponse(ClientHttpResponse response) {
        if (!log.isInfoEnabled()) {
            return;
        }
        try {
            String body = getBodyString(response);
            log.info(RESPONSE_MESSAGE, response.getStatusCode(), body);
        } catch (IOException e) {
            log.error(RESPONSE_ERROR_MESSAGE, e);
            throw new RuntimeException(e);
        }
    }

    private String getBodyString(ClientHttpResponse response) throws IOException {
        if (response != null && response.getBody() != null) {
            return IOUtils.toString(response.getBody(), UTF_8);
        } else {
            return EMPTY_MESSAGE;
        }
    }

    private void logRequest(HttpRequest httpRequest, byte[] body) {
        if (!log.isInfoEnabled()) {
            return;
        }
        log.info(REQUEST_MESSAGE, httpRequest.getURI(), httpRequest.getMethod().name(), getBody(body));
    }

    private String getBody(byte[] body) {
        return body == null ? EMPTY_MESSAGE : new String(body);
    }
}
