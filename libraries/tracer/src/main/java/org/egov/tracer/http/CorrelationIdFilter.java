package org.egov.tracer.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.egov.tracer.config.ObjectMapperFactory;
import org.egov.tracer.model.RequestContext;
import org.egov.tracer.model.RequestCorrelationId;
import org.springframework.http.MediaType;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Slf4j
public class CorrelationIdFilter implements Filter {

    private static final String FAILED_TO_DESERIALIZE_MESSAGE = "Failed to deserialize body";
    private static final String FAILED_TO_UPDATE_REQUEST_MESSAGE = "Failed to update request body with correlation id";
    private static final List<String> JSON_MEDIA_TYPES =
        Arrays.asList(MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE);
    private static final String POST = "POST";
    private static final String NO_CORRELATION_ID_IN_BODY_MESSAGE =
        "No correlation id present in the body for URI {}";
    private static final String FOUND_CORRELATION_ID_IN_BODY_MESSAGE =
        "Found correlation id {} in body for URI {}";
    private static final String NO_CORRELATION_ID_IN_HEADER_MESSAGE =
        "No correlation id found in header for URI {}";
    private static final String FOUND_CORRELATION_ID_IN_HEADER_MESSAGE =
        "Found correlation id {} in header for URI {}";
    private static final String REQUEST_BODY_LOG_MESSAGE = "Request body - {}";
    private static final String FAILED_TO_LOG_REQUEST_MESSAGE = "Failed to log request body";
    private static final String UTF_8 = "UTF-8";
    private static final String REQUEST_URI_LOG_MESSAGE = "Received request URI: {} with query strings: {}";
    private static final String LOG_RESPONSE_CODE_MESSAGE = "Response code sent: {}";
    private final ObjectMapper objectMapper;
    private boolean inboundHttpRequestBodyLoggingEnabled;

    public CorrelationIdFilter(boolean inboundHttpRequestBodyLoggingEnabled,
                               ObjectMapperFactory objectMapperFactory) {
        this.inboundHttpRequestBodyLoggingEnabled = inboundHttpRequestBodyLoggingEnabled;
        this.objectMapper = objectMapperFactory.create();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
        RequestContext.clear();
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        final MultiReadRequestWrapper wrappedRequest = new MultiReadRequestWrapper(httpRequest);
        try{
            logRequestURI(httpRequest);
            logRequestBody(wrappedRequest);
            
	        	if (isBodyCompatibleForParsing(httpRequest)) {
	            setCorrelationIdFromBody(wrappedRequest);
	        } else {
	            setCorrelationIdFromHeader(httpRequest);
	            logRequestURI(httpRequest);
	        }
        } catch (IOException ex) {
        	 ex.printStackTrace();
        } finally {
            filterChain.doFilter(wrappedRequest, servletResponse);
        }
        logResponse(servletResponse);
    }

    private void logResponse(ServletResponse servletResponse) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        log.info(LOG_RESPONSE_CODE_MESSAGE, httpServletResponse.getStatus());
    }

    private void logRequestURI(HttpServletRequest httpRequest) {
        String url = httpRequest.getRequestURL().toString();
        String queryString = httpRequest.getQueryString();
        log.info(REQUEST_URI_LOG_MESSAGE, url, queryString);
    }

    private void setCorrelationIdFromHeader(HttpServletRequest httpRequest) {
        final String correlationIdFromHeader = getCorrelationIdFromHeader(httpRequest);
        if (correlationIdFromHeader == null) {
            log.warn(NO_CORRELATION_ID_IN_HEADER_MESSAGE, httpRequest.getRequestURI());
            RequestContext.setId(getRandomCorrelationId());
        } else {
            log.info(FOUND_CORRELATION_ID_IN_HEADER_MESSAGE, correlationIdFromHeader, httpRequest.getRequestURI());
            RequestContext.setId(correlationIdFromHeader);
        }
    }

    private void setCorrelationIdFromBody(MultiReadRequestWrapper httpRequest) throws IOException {
        final RequestCorrelationId correlationIdFromRequestBody = getCorrelationIdFromRequestBody(httpRequest);
        final String correlationId = correlationIdFromRequestBody.get();
        if (correlationId == null) {
            log.warn(NO_CORRELATION_ID_IN_BODY_MESSAGE, httpRequest.getRequestURI());
            setCorrelationIdFromHeader(httpRequest);
            setCorrelationIdToRequestBody(httpRequest, correlationIdFromRequestBody);
        } else {
            log.info(FOUND_CORRELATION_ID_IN_BODY_MESSAGE, correlationIdFromRequestBody, httpRequest.getRequestURI());
            RequestContext.setId(correlationId);
        }
    }

    private boolean isBodyCompatibleForParsing(HttpServletRequest httpRequest) {
        return POST.equals(httpRequest.getMethod())
            && JSON_MEDIA_TYPES.contains(httpRequest.getContentType());
    }

    private String getCorrelationIdFromHeader(HttpServletRequest httpRequest) {
        return httpRequest.getHeader(RequestHeader.CORRELATION_ID);
    }

    private RequestCorrelationId getCorrelationIdFromRequestBody(MultiReadRequestWrapper httpRequest) throws IOException {
        try {
            @SuppressWarnings("unchecked") final HashMap<String, Object> requestBody = (HashMap<String, Object>)
                objectMapper.readValue(httpRequest.getInputStream(), HashMap.class);
            return new RequestCorrelationId(requestBody);
        } catch (IOException e) {
            log.error(FAILED_TO_DESERIALIZE_MESSAGE, e);
            
            // We can't return the correlation in body when it is not parsable
            throw e;
            //return new RequestCorrelationId(null);
        }
    }

    private void setCorrelationIdToRequestBody(MultiReadRequestWrapper httpRequest,
                                               RequestCorrelationId requestCorrelationId) {
        try {
            updateRequestInfoWithCorrelationId(httpRequest, requestCorrelationId);
        } catch (IOException e) {
            log.error(FAILED_TO_UPDATE_REQUEST_MESSAGE, e);
        }
    }

    private void updateRequestInfoWithCorrelationId(MultiReadRequestWrapper httpRequest,
                                                    RequestCorrelationId requestCorrelationId) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final String correlationId = RequestContext.getId();
        final HashMap<String, Object> updatedRequestInfo = requestCorrelationId.update(correlationId);
        objectMapper.writeValue(out, updatedRequestInfo);
        httpRequest.update(out);
    }

    private void logRequestBody(MultiReadRequestWrapper requestWrapper) {
        if (inboundHttpRequestBodyLoggingEnabled) {
            try {
                final String requestBody = IOUtils.toString(requestWrapper.getInputStream(), UTF_8);
                log.info(REQUEST_BODY_LOG_MESSAGE, requestBody);
            } catch (IOException e) {
                log.error(FAILED_TO_LOG_REQUEST_MESSAGE, e);
            }
        }
    }

    @Override
    public void destroy() {

    }

    private String getRandomCorrelationId() {
        return UUID.randomUUID().toString();
    }

}
