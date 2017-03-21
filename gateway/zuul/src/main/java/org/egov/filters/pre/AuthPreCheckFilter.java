package org.egov.filters.pre;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.egov.wrapper.CustomRequestWrapper;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.*;

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
        String authToken;
        RequestContext ctx = RequestContext.getCurrentContext();

        if (openEndpointsWhitelist.contains(ctx.getRequest().getRequestURI())) {
            setShouldDoAuth(ctx, false);
            return null;
        }

        try {
            authToken = getAuthTokenFromRequest(ctx);
            ctx.set("authToken", authToken);
            if (authToken == null) {
                if (anonymousEndpointsWhitelist.contains(ctx.getRequest().getRequestURI())) {
                    setShouldDoAuth(ctx, false);
                } else {
                    abortWithStatus(ctx, HttpStatus.UNAUTHORIZED, "You are not authorized to access this resource");
                    return null;
                }
            } else {
                setShouldDoAuth(ctx, true);
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            abortWithStatus(ctx, HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong");
            return null;
        }
    }

    private String getAuthTokenFromRequest(RequestContext ctx) throws IOException {
        if (Objects.equals(ctx.getRequest().getMethod().toUpperCase(), "GET") ||
                ctx.getRequest().getRequestURI().matches("^/filestore/.*")) {
            return getAuthTokenFromRequestHeader(ctx);
        } else {
            return getAuthTokenFromRequestBody(ctx);
        }
    }

    private String getAuthTokenFromRequestBody(RequestContext ctx) throws IOException {
        CustomRequestWrapper requestWrapper = new CustomRequestWrapper(ctx.getRequest());

        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
        };
        HashMap<String, Object> requestBody = objectMapper.readValue(requestWrapper.getPayload(), typeRef);
        HashMap<String, String> requestInfo = (HashMap<String, String>) requestBody.get("RequestInfo");
        if (requestInfo == null) {
            return null;
        }
        sanitizeAndSetRequest(ctx, requestBody, objectMapper, requestWrapper);
        return requestInfo.get("authToken");
    }

    private void sanitizeAndSetRequest(RequestContext ctx, HashMap<String, Object> requestBody, ObjectMapper objectMapper,
                                       CustomRequestWrapper requestWrapper) throws JsonProcessingException {
        HashMap<String, Object> requestInfo = (HashMap<String, Object>) requestBody.get("RequestInfo");

        requestInfo.remove("userInfo");
        requestBody.put("RequestInfo", requestInfo);
        requestWrapper.setPayload(objectMapper.writeValueAsString(requestBody));
        ctx.setRequest(requestWrapper);
    }

    private String getAuthTokenFromRequestHeader(RequestContext ctx) {
        return ctx.getRequest().getHeader("auth-token");
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