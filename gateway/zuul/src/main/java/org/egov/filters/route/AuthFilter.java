package org.egov.filters.route;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.client.ClientException;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.io.IOUtils;
import org.egov.model.AuthRequest;
import org.egov.model.CustomHttpServletRequestWrapper;
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
import java.util.*;

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

@Component
public class AuthFilter extends ZuulFilter {
    private static Logger log = LoggerFactory.getLogger(AuthFilter.class);

    private ProxyRequestHelper helper = new ProxyRequestHelper();
    private RibbonRoutingFilter delegateFilter;
    private HttpServletRequest request;
    private CustomHttpServletRequestWrapper customHttpServletRequestWrapper;
    private List<RibbonRequestCustomizer> requestCustomizers = new ArrayList<>();

    @Autowired
    RibbonCommandFactory<?> ribbonCommandFactory;

    @PostConstruct
    void initDelegateFilter() {
        delegateFilter = new RibbonRoutingFilter(helper, ribbonCommandFactory, requestCustomizers);
        customHttpServletRequestWrapper =
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
        String requestUri = request.getRequestURI();
        return delegateFilter.shouldFilter() && !requestUri.contains("/user");
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest originalRequest = ctx.getRequest();
        String authToken = originalRequest.getHeader("auth_token");
        if (authToken != null) {
            try {
                HashMap<String, List<String>> originalRequestQueryParams = (HashMap<String, List<String>>) ctx.getRequestQueryParams();
                String originalRequestUri = (String) ctx.get("requestURI");
                AuthRequest authRequest = new AuthRequest();
                HashMap<String, List<String>> parameters = new HashMap<>();

                List<String> paramValues = new ArrayList<>();
                paramValues.add(authToken);
                parameters.put("access_token", paramValues);

                ctx.set("serviceId", "users");
                ctx.setRequestQueryParams(parameters);
                ctx.set("requestURI", "/user/_details");
                ctx.setRequest(authRequest);

                ClientHttpResponse authResponse = (ClientHttpResponse) delegateFilter.run();

                ctx.setRequestQueryParams(originalRequestQueryParams);
                ctx.set("requestURI", originalRequestUri);
                ctx.setRequest(originalRequest);

                if (isAuthenticationSuccessful(authResponse)) {
                    Integer userId = getUserIdFromAuthResponse(authResponse);
                    ctx.addZuulRequestHeader("requester_id", userId.toString());
                    return delegateFilter.run();
                }

                setResponse(authResponse);
                ctx.setSendZuulResponse(false);
            } catch (Exception e) {
                ctx.set("error.status_code", SC_INTERNAL_SERVER_ERROR);
                ctx.set("error.exception", e);
            }
        }

        return null;
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