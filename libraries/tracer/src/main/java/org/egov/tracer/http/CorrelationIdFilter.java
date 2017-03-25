package org.egov.tracer.http;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.egov.tracer.model.RequestContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

@Slf4j
public class CorrelationIdFilter implements Filter {

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
        final MultiReadableRequestWrapper wrappedRequest = new MultiReadableRequestWrapper(httpRequest);
        setCorrelationId(wrappedRequest);
        filterChain.doFilter(wrappedRequest, servletResponse);
    }

    private void setCorrelationId(HttpServletRequest httpRequest) throws IOException {
        if ("GET".equals(httpRequest.getMethod())) {
            RequestContext.setId(getRandomCorrelationId());
        }
        final String correlationIdFromRequest = getCorrelationIdFromRequest(httpRequest);
        if (correlationIdFromRequest == null) {
            RequestContext.setId(getRandomCorrelationId());
        } else {
            RequestContext.setId(correlationIdFromRequest);
        }
    }

    private String getCorrelationIdFromRequest(HttpServletRequest httpRequest) throws IOException {
        try {
            final HashMap hashMap = objectMapper.readValue(httpRequest.getInputStream(), HashMap.class);
            final HashMap<String, Object> requestInfo = getRequestInfo(hashMap);
            if (requestInfo != null) {
                return (String) requestInfo.get("correlationId");
            }
        } catch (JsonParseException | JsonMappingException e) {
            log.error("Failed to deserialize body", e);
            return null;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private HashMap<String, Object> getRequestInfo(HashMap hashMap) {
        final Object requestInfo1 = hashMap.get("RequestInfo");
        if (requestInfo1 == null) {
            final Object requestInfo2 = hashMap.get("requestInfo");
            return (HashMap<String, Object> )requestInfo2;
        }
        return (HashMap<String, Object>) requestInfo1;
    }

    @Override
    public void destroy() {

    }

    private String getRandomCorrelationId() {
        return UUID.randomUUID().toString();
    }

}
