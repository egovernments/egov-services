package org.egov.workflow.web.interceptor;

import org.egov.workflow.domain.model.RequestContext;
import org.slf4j.MDC;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class CorrelationIdInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        final String correlationId = getCorrelationId(request);
        MDC.put(RequestContext.CORRELATION_ID, correlationId);
        RequestContext.setId(correlationId);
        return super.preHandle(request, response, handler);
    }

    private String getCorrelationId(HttpServletRequest request) {
        final String incomingCorrelationId = request.getHeader(RequestContext.CORRELATION_ID);
        return incomingCorrelationId == null ? UUID.randomUUID().toString() : incomingCorrelationId;
    }
}
