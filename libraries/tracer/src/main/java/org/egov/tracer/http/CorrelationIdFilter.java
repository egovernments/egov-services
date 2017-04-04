package org.egov.tracer.http;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.RequestContext;
import org.egov.tracer.model.RequestCorrelationId;
import org.springframework.http.MediaType;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Slf4j
public class CorrelationIdFilter implements Filter {

    private static final String CORRELATION_ID_HEADER_NAME = "X-CORRELATION-ID";
    private static final String FAILED_TO_DESERIALIZE_MESSAGE = "Failed to deserialize body";
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
    private final ObjectMapper objectMapper;

    public CorrelationIdFilter() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
        RequestContext.clear();
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        if (isBodyCompatibleForParsing(httpRequest)) {
            final MultiReadRequestWrapper wrappedRequest = new MultiReadRequestWrapper(httpRequest);
            setCorrelationIdFromBody(wrappedRequest);
            filterChain.doFilter(wrappedRequest, servletResponse);
        } else {
            setCorrelationIdFromHeader(httpRequest);
            filterChain.doFilter(servletRequest, servletResponse);
        }
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

    private void setCorrelationIdFromBody(HttpServletRequest httpRequest) throws IOException {
        final String correlationIdFromRequestBody = getCorrelationIdFromRequestBody(httpRequest);
        if (correlationIdFromRequestBody == null) {
            log.warn(NO_CORRELATION_ID_IN_BODY_MESSAGE, httpRequest.getRequestURI());
            RequestContext.setId(getRandomCorrelationId());
        } else {
            log.info(FOUND_CORRELATION_ID_IN_BODY_MESSAGE, correlationIdFromRequestBody, httpRequest.getRequestURI());
            RequestContext.setId(correlationIdFromRequestBody);
        }
    }

    private boolean isBodyCompatibleForParsing(HttpServletRequest httpRequest) {
        return POST.equals(httpRequest.getMethod())
        && JSON_MEDIA_TYPES.contains(httpRequest.getContentType());
    }

    private String getCorrelationIdFromHeader(HttpServletRequest httpRequest) {
        return httpRequest.getHeader(CORRELATION_ID_HEADER_NAME);
    }

    private String getCorrelationIdFromRequestBody(HttpServletRequest httpRequest) throws IOException {
        try {
            @SuppressWarnings("unchecked")
            final HashMap<String, Object> hashMap = (HashMap<String, Object>)
                objectMapper.readValue(httpRequest.getInputStream(), HashMap.class);
            return new RequestCorrelationId(hashMap).get();
        } catch (JsonParseException | JsonMappingException e) {
            log.error(FAILED_TO_DESERIALIZE_MESSAGE, e);
            return null;
        }
    }

    @Override
    public void destroy() {

    }

    private String getRandomCorrelationId() {
        return UUID.randomUUID().toString();
    }

}
