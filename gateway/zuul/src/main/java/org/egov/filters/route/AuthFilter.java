package org.egov.filters.route;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.client.ClientException;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.io.IOUtils;
import org.egov.model.AuthRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.support.RibbonRequestCustomizer;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandFactory;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonRoutingFilter;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

@Component
public class AuthFilter extends ZuulFilter {
    private static Logger log = LoggerFactory.getLogger(AuthFilter.class);

    private ProxyRequestHelper helper = new ProxyRequestHelper();
    private RibbonRoutingFilter delegateFilter;
    private HttpServletRequest originalRequest;
    private String authToken;
    private String originalRequestUri;
    private URL originalRouteHost;
    private HashMap<String, List<String>> originalRequestQueryParams;
        private List<RibbonRequestCustomizer> requestCustomizers = new ArrayList<>();

    @Autowired
    RibbonCommandFactory<?> ribbonCommandFactory;

    public AuthFilter() {
    }

    @PostConstruct
    void initDelegateFilter() {
        delegateFilter = new RibbonRoutingFilter(helper, ribbonCommandFactory, requestCustomizers);
    }

    @Override
    public String filterType() {
        return "route";
    }

    @Override
    public int filterOrder() {
        return delegateFilter.filterOrder();
    }

    @Override
    public boolean shouldFilter() {
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        return !request.getRequestURI().contains("/user") && request.getHeader("auth_token") != null;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        try {
            saveOriginalRequestItems(ctx);
            prepareToRouteToAuthService(ctx);
            ClientHttpResponse authResponse = (ClientHttpResponse) delegateFilter.run();
            prepareToRouteToOriginalService(ctx);

            if (isAuthenticationSuccessful(authResponse)) {
                Integer userId = getUserIdFromAuthResponse(authResponse);
                ctx.addZuulRequestHeader("requester_id", userId.toString());
                return delegateFilter.run();
            } else {
                logError(ctx);
                abort(ctx, authResponse);
            }
        } catch (Exception e) {
            ctx.set("error.status_code", SC_INTERNAL_SERVER_ERROR);
            ctx.set("error.exception", e);
            logError(ctx);
        }

        return null;
    }

    private void saveOriginalRequestItems(RequestContext ctx) {
        originalRequestQueryParams = (HashMap<String, List<String>>) ctx.getRequestQueryParams();
        originalRequest = ctx.getRequest();
        originalRequestUri = originalRequest.getRequestURI();
        originalRouteHost = ctx.getRouteHost();
        authToken = originalRequest.getHeader("auth_token");
    }

    private void logError(RequestContext ctx) {
        log.error("ERROR");
        log.error(String.format("STATUS_CODE: %s", ctx.get("error.status_code")));
        log.error(String.format("ERROR_MESSAGE: %s", ctx.get("error.message")));
        if (ctx.get("error.exception") != null) {
            Exception ex = (Exception) ctx.get("error.exception");
            String stackTrace = Arrays.toString(ex.getStackTrace());
            log.error(String.format("TRACE: %s", stackTrace));
        }
    }

    private void abort(RequestContext ctx, ClientHttpResponse authResponse) throws ClientException, IOException {
        if (authResponse != null) {
            setResponse(authResponse);
        }
        ctx.setSendZuulResponse(false);
    }

    private void prepareToRouteToOriginalService(RequestContext ctx) throws MalformedURLException {
        ctx.setRouteHost(new URL(originalRouteHost.getProtocol(), originalRouteHost.getHost(),
                originalRouteHost.getPort(), "/"));
        ctx.setRequestQueryParams(originalRequestQueryParams);
        ctx.set("requestURI", originalRequestUri);
        ctx.setRequest(originalRequest);

    }

    private void prepareToRouteToAuthService(RequestContext ctx) {
        AuthRequestWrapper authRequestWrapper = new AuthRequestWrapper(ctx.getRequest());
        HashMap<String, List<String>> parameters = new HashMap<>();
        List<String> paramValues = new ArrayList<>();
        paramValues.add(authToken);
        parameters.put("access_token", paramValues);

        ctx.set("serviceId", "user");
        ctx.setRequestQueryParams(parameters);
        ctx.set("requestURI", "/user/_details");
        ctx.setRequest(authRequestWrapper);
    }

    private Integer getUserIdFromAuthResponse(ClientHttpResponse authResponse) throws IOException {
        String authResponseBody = IOUtils.toString(authResponse.getBody(), "utf-8");
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> requestMap = mapper.readValue(authResponseBody, new TypeReference<Map<String, Object>>() {
        });
        return (Integer) requestMap.get("id");
    }

    private boolean isAuthenticationSuccessful(ClientHttpResponse response) throws IOException {
        return response != null && response.getStatusCode().is2xxSuccessful();
    }

    private void setResponse(ClientHttpResponse resp) throws ClientException, IOException {
        helper.setResponse(resp.getStatusCode().value(),
                resp.getBody() == null ? null : resp.getBody(), resp.getHeaders());
    }

}