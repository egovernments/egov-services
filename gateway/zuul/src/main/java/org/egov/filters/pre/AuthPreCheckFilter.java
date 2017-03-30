package org.egov.filters.pre;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.egov.wrapper.CustomRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.*;

public class AuthPreCheckFilter extends ZuulFilter {
    private HashSet<String> openEndpointsWhitelist;
    private HashSet<String> anonymousEndpointsWhitelist;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

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
            logger.info(String.format("pre-auth-filter: Routing to an open endpoint: %s", ctx.getRequest().getRequestURI()));
            return null;
        }

        try {
            authToken = getAuthTokenFromRequest(ctx);
            ctx.set("authToken", authToken);
            logger.info(String.format("pre-auth-filter: Auth-token: %s", authToken));
            if (authToken == null) {
                if (anonymousEndpointsWhitelist.contains(ctx.getRequest().getRequestURI())) {
                    logger.info(String.format("pre-auth-filter: Routing to an anonymous endpoint: %s, No auth provided", ctx.getRequest().getRequestURI()));
                    setShouldDoAuth(ctx, false);
                } else {
                    logger.info(String.format("pre-auth-filter: Routing to an non-anonymous endpoint: %s, No auth provided", ctx.getRequest().getRequestURI()));
                    abortWithStatus(ctx, HttpStatus.UNAUTHORIZED, "You are not authorized to access this resource");
                    return null;
                }
            } else {
                logger.info(String.format("pre-auth-filter: Routing to an endpoint: %s, Auth provided", ctx.getRequest().getRequestURI()));
                setShouldDoAuth(ctx, true);
            }
            return null;
        } catch (IOException e) {
            logger.info(String.format("pre-auth-filter: Error while routing to an endpoint: %s", ctx.getRequest().getRequestURI()));
            e.printStackTrace();
            abortWithStatus(ctx, HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong");
            return null;
        }
    }

    private String getAuthTokenFromRequest(RequestContext ctx) throws IOException {
        if (Objects.equals(ctx.getRequest().getMethod().toUpperCase(), "GET") ||
                ctx.getRequest().getRequestURI().matches("^/filestore/.*")) {
            logger.info("pre-auth-filter: Getting auth-token from header");
            return getAuthTokenFromRequestHeader(ctx);
        } else {
            logger.info("pre-auth-filter: Getting auth-token from request body");
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
            logger.info("pre-auth-filter: no request-info, so no authToken");
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
        logger.info(String.format("pre-auth-filter: Updated request payload - %s", requestWrapper.getPayload()));
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