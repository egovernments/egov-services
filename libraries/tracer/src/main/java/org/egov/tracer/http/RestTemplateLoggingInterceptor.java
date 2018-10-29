package org.egov.tracer.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpMessage;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class RestTemplateLoggingInterceptor implements ClientHttpRequestInterceptor {

    private static final String REQUEST_MESSAGE_WITH_BODY = "Sending request to {} with verb {} with body {}";
    private static final String REQUEST_MESSAGE = "Sending request to {} with verb {}";
    private static final String RESPONSE_MESSAGE_WITH_BODY = "Received from {} response code {} and body {}: ";
    private static final String RESPONSE_MESSAGE = "Received response from {}";
    private static final String FAILED_RESPONSE_MESSAGE = "Received error response from %s";
    private static final String UTF_8 = "UTF-8";
    private static final String RESPONSE_BODY_ERROR_MESSAGE = "Error reading response body";
    private static final String EMPTY_BODY = "<NOT-AVAILABLE>";
    private static final List<String> JSON_MEDIA_TYPES =
        Arrays.asList(MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        try {
            logRequest(request, body);

            final ClientHttpResponse rawResponse = execution.execute(request, body);

            if(log.isDebugEnabled() && isBodyCompatibleForParsing(request)){
                logResponse(rawResponse, request);
            } else{
                log.info(RESPONSE_MESSAGE, request.getURI());
            }
            return rawResponse;
        } catch (Exception e) {
            log.warn(String.format(FAILED_RESPONSE_MESSAGE, request.getURI()), e);
            throw e;
        }
    }

    private void logResponse(ClientHttpResponse response, HttpRequest httpRequest) throws IOException {

        if(log.isDebugEnabled() && isBodyCompatibleForParsing(httpRequest)){
            String body = getBodyString(response);
            log.debug(RESPONSE_MESSAGE_WITH_BODY, httpRequest.getURI(), response.getStatusCode(), body);
        } else{
            log.info(RESPONSE_MESSAGE, httpRequest.getURI());
        }

    }

    private void logRequest(HttpRequest httpRequest, byte[] body) {
        if(log.isDebugEnabled() && isBodyCompatibleForParsing(httpRequest)){
            log.debug(REQUEST_MESSAGE_WITH_BODY, httpRequest.getURI(), httpRequest.getMethod().name(), getBody(body));
        }
        else {
            log.info(REQUEST_MESSAGE, httpRequest.getURI(), httpRequest.getMethod().name());
        }
    }


    private String getBody(byte[] body) {
        return body == null ? EMPTY_BODY : new String(body);
    }

    private boolean isBodyCompatibleForParsing(HttpMessage httpMessage) {
        final MediaType contentType = httpMessage.getHeaders().getContentType();
        return contentType != null && JSON_MEDIA_TYPES.contains(contentType.toString());
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

}
