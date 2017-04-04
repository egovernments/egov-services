package org.egov.filters.post;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

import static org.egov.constants.RequestContextConstants.CORRELATION_ID_KEY;

@Component
public class ResponseEnhancementFilter extends ZuulFilter {

    private static final String CORRELATION_HEADER_NAME = "x-correlation-id";

    @Override
    public String filterType() {
        return "post";
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
        ctx.addZuulResponseHeader(CORRELATION_HEADER_NAME, getCorrelationId());
        return null;
    }

    private String getCorrelationId() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return (String) ctx.get(CORRELATION_ID_KEY);
    }
}
