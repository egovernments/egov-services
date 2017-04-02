package org.egov.filters.pre;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CorrelationIdFilter extends ZuulFilter {

    private static final String CORRELATION_HEADER_NAME = "x-correlation-id";
    private static final String RECEIVED_REQUEST_MESSAGE = "Received request for: ";
    private static final String CORRELATION_ID = "CORRELATION_ID";

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        final String correlationId = UUID.randomUUID().toString();
        ctx.set(CORRELATION_ID, correlationId);
        ctx.addZuulRequestHeader(CORRELATION_HEADER_NAME, correlationId);
        MDC.put(CORRELATION_ID, correlationId);
        logger.info(RECEIVED_REQUEST_MESSAGE + ctx.getRequest().getRequestURI());
        return null;
    }

}