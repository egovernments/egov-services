package org.egov.filters.pre;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;

public class AuthPreCheckFilter extends ZuulFilter {
    private HashSet<String> openEndpointsWhitelist;
    private HashSet<String> anonymousEndpointsWhitelist;

    public AuthPreCheckFilter(HashSet<String> openEndpointsWhitelist,
                              HashSet<String> anonymousEndpointsWhitelist) {
        this.openEndpointsWhitelist = openEndpointsWhitelist;
        this.anonymousEndpointsWhitelist = anonymousEndpointsWhitelist;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String authToken = ctx.getRequest().getHeader("auth-token");

        HttpServletRequest request = ctx.getRequest();
        if (openEndpointsWhitelist.contains(request.getRequestURI())) {
            setShouldDoAuth(ctx, false);
            return null;
        }

        System.out.println(anonymousEndpointsWhitelist.toString());
        if (authToken == null) {
            if (anonymousEndpointsWhitelist.contains(request.getRequestURI())) {
                setShouldDoAuth(ctx, false);
                return null;
            } else {
                abortWithStatus(ctx, HttpStatus.UNAUTHORIZED, "You are not authorized to access this resource");
                return null;
            }
        }

        setShouldDoAuth(ctx, true);
        return null;
    }

    private void setShouldDoAuth(RequestContext ctx, boolean b) {
        ctx.set("shouldDoAuth", b);
    }

    private void abortWithStatus(RequestContext ctx, HttpStatus status, String message) {
        ctx.set("error.status_code", status.value());
        ctx.set("error.message", message);
        ctx.setSendZuulResponse(false);
    }
}