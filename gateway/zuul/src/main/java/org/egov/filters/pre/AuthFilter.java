package org.egov.filters.pre;


import com.netflix.client.ClientException;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandFactory;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonRoutingFilter;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

@Component
public class AuthFilter extends ZuulFilter {
    private static Logger log = LoggerFactory.getLogger(AuthFilter.class);

    private ProxyRequestHelper helper = new ProxyRequestHelper();
    private RibbonRoutingFilter delegateFilter;

    @Autowired
    RibbonCommandFactory<?> ribbonCommandFactory;

    @PostConstruct
    void initDelegateFilter() {
        delegateFilter = new RibbonRoutingFilter(helper, ribbonCommandFactory, null);
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
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.info(String.format("%s fuuuuubaaarrreedd  request to %s", request.getMethod(), request.getRequestURL().toString()));
        String authToken = ctx.getRequest().getHeader("auth_token");
        log.info(String.format("$$$$$$$$$$$$$$$$$$$$$$$$$$ %s", ctx.getRequest().getRequestURI()));
        log.info(String.format("$$$$$$$$$$$$$$$$$$$$$$$$$$ %s", ctx.getRouteHost()));
        log.info(String.format("$$$$$$$$$$$$$$$$$$$$$$$$$$ %s", ctx.getRequest().getQueryString()));
        if (authToken != null) {
            try {
                URL originalRouteHost = ctx.getRouteHost();
                String originalServiceId = (String) ctx.get("serviceId");
                URL authRouteHost = new URL("http", "localhost", 8082, "");
                ctx.setRouteHost(authRouteHost);
                ctx.set("serviceId", "localhost:8082");
                log.info(String.format("$$$$$$$$$$$$$$$$$$$$$$$$$$ %s", authRouteHost.getPath()));
                HashMap<String, List<String>> parameters = new HashMap<>();
                List<String> paramValues = new ArrayList<>();
                paramValues.add(authToken);
                parameters.put("access_token", paramValues);
                ctx.setRequestQueryParams(parameters);
                ctx.set("requestURI", "/user/_details");
                ClientHttpResponse authResponse = (ClientHttpResponse) delegateFilter.run();
                log.info("+++++++++++");
                ctx.set("serviceId", originalServiceId);
                ctx.setRouteHost(originalRouteHost);
                log.info(ctx.get("error.status_code").toString());
                log.info(new Exception((Throwable) ctx.get("error.exception")).getMessage());
                log.info(String.format("@@@@@@@@@@@@@, %s", authResponse));
                if (isAuthenticationSuccessful(authResponse)) {
                    return delegateFilter.run();
                }

                setResponse(authResponse);

                return authResponse;
            } catch (Exception e) {
                ctx.set("error.status_code", SC_INTERNAL_SERVER_ERROR);
                ctx.set("error.exception", e);
            }
        }

        return null;
    }

    private boolean isAuthenticationSuccessful(ClientHttpResponse response) throws IOException {
        return response != null && response.getStatusCode().is2xxSuccessful();
    }

    private void setResponse(ClientHttpResponse resp) throws ClientException, IOException {
        helper.setResponse(resp.getStatusCode().value(),
                resp.getBody() == null ? null : resp.getBody(), resp.getHeaders());
    }

}