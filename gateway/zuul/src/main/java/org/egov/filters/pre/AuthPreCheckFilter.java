package org.egov.filters.pre;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

public class AuthPreCheckFilter extends ZuulFilter {
    private String openEndpointsWhitelist;
    private String anonymousEndpointsWhitelist;

    public AuthPreCheckFilter(String openEndpointsWhitelist,
                              String anonymousEndpointsWhitelist) {
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
        if (isRoutingToAnyWhitelistedEndpoints(request, openEndpointsWhitelist)) {
            setShouldDoAuth(ctx, false);
            return null;
        }

        if (authToken == null) {
            if (isRoutingToAnyWhitelistedEndpoints(request, anonymousEndpointsWhitelist)) {
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

    private boolean isRoutingToAnyWhitelistedEndpoints(HttpServletRequest request, String whitelist) {
        for (String openEndpoint : whitelist.split(",")) {
            if (request.getRequestURI().contains(openEndpoint)) {
                return true;
            }
        }
        return false;
    }

    private void abortWithStatus(RequestContext ctx, HttpStatus status, String message) {
        ctx.set("error.status_code", status.value());
        ctx.set("error.message", message);
        ctx.setSendZuulResponse(false);
    }
}