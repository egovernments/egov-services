package org.egov.filters.pre;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CorrelationIdFilter extends ZuulFilter {

	public static final String CORRELATION_HEADER_NAME = "x-correlation-id";
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
        ctx.addZuulRequestHeader(CORRELATION_HEADER_NAME, UUID.randomUUID().toString());
        logger.info("Received request for " + ctx.getRequest().getRequestURI());
		return null;
    }

}