package org.egov.tracer.http;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.RequestContext;
import org.egov.tracer.model.RequestCorrelationId;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

@Slf4j
public class CorrelationIdFilter implements Filter {

    private static final String CORRELATION_ID_HEADER_NAME = "X-CORRELATION-ID";
    private static final String FAILED_TO_DESERIALIZE_MESSAGE = "Failed to deserialize body";
    private static final String GET = "GET";
    private static final String EMPTY_STRING = "";
    private static final String MULTI_PART = "multipart";
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
        if (isBodyNotCompatibleForParsing(httpRequest)) {
            setCorrelationIdFromHeader(httpRequest);
            addCorrelationIdToResponseHeader(servletResponse);
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            final MultiReadRequestWrapper wrappedRequest = new MultiReadRequestWrapper(httpRequest);
            setCorrelationIdFromBody(wrappedRequest);
            addCorrelationIdToResponseHeader(servletResponse);
            filterChain.doFilter(wrappedRequest, servletResponse);
        }
    }

    private void addCorrelationIdToResponseHeader(ServletResponse servletResponse) {
        ((HttpServletResponse) servletResponse).addHeader(CORRELATION_ID_HEADER_NAME, RequestContext.getId());
    }

    private void setCorrelationIdFromHeader(HttpServletRequest httpRequest) {
        final String correlationIdFromHeader = getCorrelationIdFromHeader(httpRequest);
        if (correlationIdFromHeader == null) {
            RequestContext.setId(getRandomCorrelationId());
        } else {
            RequestContext.setId(correlationIdFromHeader);
        }
    }

    private void setCorrelationIdFromBody(HttpServletRequest httpRequest) throws IOException {
        final String correlationIdFromRequestBody = getCorrelationIdFromRequestBody(httpRequest);
        if (correlationIdFromRequestBody == null) {
            RequestContext.setId(getRandomCorrelationId());
        } else {
            RequestContext.setId(correlationIdFromRequestBody);
        }
    }

    private boolean isBodyNotCompatibleForParsing(HttpServletRequest httpRequest) {
        return GET.equals(httpRequest.getMethod()) || isMultiPartContentType(httpRequest);
    }

    private boolean isMultiPartContentType(HttpServletRequest httpRequest) {
        return getContentType(httpRequest).indexOf(MULTI_PART) > 0;
    }

    private String getContentType(HttpServletRequest httpRequest) {
        return httpRequest.getContentType() == null ? EMPTY_STRING : httpRequest.getContentType().toLowerCase();
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
