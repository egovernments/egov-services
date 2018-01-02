package org.egov.filter.error;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogErrorFilter extends ZuulFilter {

   

    @Override
    public String filterType() {
        return "error";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return RequestContext.getCurrentContext().getThrowable() != null;
    }

    @Override
    public Object run() {
    	System.out.println("LogErrorFilter");
        Throwable throwable = RequestContext.getCurrentContext().getThrowable();
        throwable.printStackTrace();
        log.error("Exception was thrown in filters: ", throwable);
        return null;
    }
}