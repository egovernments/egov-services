package org.egov.tracer.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.egov.tracer.config.TracerProperties;
import org.springframework.http.HttpMessage;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
public class LogAwareRestTemplate extends RestTemplate {

    private static final String REQUEST_MESSAGE_WITH_BODY = "Sending request to {} with verb {} with body {}";
    private static final String REQUEST_MESSAGE = "Sending request to {} with verb {}";
    private static final String RESPONSE_MESSAGE_WITH_BODY = "Received from {} response code {} and body {}: ";
    private static final String RESPONSE_MESSAGE = "Received response from {}";
    private static final String FAILED_RESPONSE_MESSAGE = "Received error response from %s";
    private static final String UTF_8 = "UTF-8";
    private static final String RESPONSE_BODY_ERROR_MESSAGE = "Error reading response body";
    private static final String RESPONSE_CODE_ERROR_MESSAGE = "Error reading response code";
    private static final String EMPTY_BODY = "<NOT-AVAILABLE>";
    private static final List<String> JSON_MEDIA_TYPES =
        Arrays.asList(MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE);
    private TracerProperties tracerProperties;

    public LogAwareRestTemplate(ClientHttpRequestFactory requestFactory, TracerProperties tracerProperties) {
        super(requestFactory);
        this.tracerProperties = tracerProperties;
        this.setInterceptors(Collections.singletonList(logRequestAndResponse()));
    }


    private ClientHttpRequestInterceptor logRequestAndResponse() {
        return (httpRequest, body, clientHttpRequestExecution) -> {
            logRequest(httpRequest, body);
            return executeAndLogResponse(httpRequest, body, clientHttpRequestExecution);
        };
    }

    private ClientHttpResponse executeAndLogResponse(HttpRequest httpRequest, byte[] body, ClientHttpRequestExecution
        clientHttpRequestExecution) throws IOException {
        try {
            final ClientHttpResponse rawResponse = clientHttpRequestExecution.execute(httpRequest, body);
            if (tracerProperties.isDetailedTracingDisabled() || isBodyNotCompatibleForParsing(httpRequest)) {
                log.info(RESPONSE_MESSAGE, httpRequest.getURI());
                return rawResponse;
            }
            final CacheableSimpleClientHttpResponse response = new CacheableSimpleClientHttpResponse(rawResponse);
            logResponse(response, httpRequest.getURI());
            return response;
        } catch (Exception e) {
            log.error(String.format(FAILED_RESPONSE_MESSAGE, httpRequest.getURI()), e);
            throw e;
        }
    }

    private boolean isBodyNotCompatibleForParsing(HttpMessage httpMessage) {
        final MediaType contentType = httpMessage.getHeaders().getContentType();
        return contentType == null || !JSON_MEDIA_TYPES.contains(contentType.toString());
    }

    private void logResponse(ClientHttpResponse response, URI uri) {
        try {
            String body = getBodyString(response);
            log.info(RESPONSE_MESSAGE_WITH_BODY, uri, response.getStatusCode(), body);
        } catch (IOException e) {
            log.error(RESPONSE_CODE_ERROR_MESSAGE, e);
        }
    }

    private String getBodyString(ClientHttpResponse response) {
        try {
            if (response != null && response.getBody() != null) {
                return IOUtils.toString(response.getBody(), UTF_8);
            } else {
                return EMPTY_BODY;
            }
        } catch (IOException e) {
            log.error(RESPONSE_BODY_ERROR_MESSAGE, e);
            return EMPTY_BODY;
        }
    }

    private void logRequest(HttpRequest httpRequest, byte[] body) {
        if (tracerProperties.isDetailedTracingDisabled() || isBodyNotCompatibleForParsing(httpRequest)) {
            log.info(REQUEST_MESSAGE, httpRequest.getURI(), httpRequest.getMethod().name());
        } else {
            log.info(REQUEST_MESSAGE_WITH_BODY, httpRequest.getURI(), httpRequest.getMethod().name(), getBody(body));
        }
    }

    private String getBody(byte[] body) {
        return body == null ? EMPTY_BODY : new String(body);
    }

}