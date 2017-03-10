package org.egov.filters.pre;


import com.netflix.client.ClientException;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.HttpServletRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.route.SimpleHostRoutingFilter;
import org.springframework.http.client.ClientHttpResponse;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URL;

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

public class AuthFilter extends ZuulFilter {
    private static Logger log = LoggerFactory.getLogger(AuthFilter.class);

    private ProxyRequestHelper helper = new ProxyRequestHelper();
    private SimpleHostRoutingFilter delegateFilter;

    @Value("${user.service.url}")
    private String userServiceUrl;

    @Autowired
    private ZuulProperties zuulProperties;

    @PostConstruct
    void initDelegateFilter() {
        delegateFilter = new SimpleHostRoutingFilter(helper, zuulProperties);
    }

    @Override
    public String filterType() {
        return "route";
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
        HttpServletRequest request = ctx.getRequest();
        log.info(String.format("%s fuuuuubaaarrreedd  request to %s", request.getMethod(), request.getRequestURL().toString()));
        String authToken = ctx.getRequest().getHeader("auth_token");
        log.info(String.format("$$$$$$$$$$$$$$$$$$$$$$$$$$ %s", ctx.getRequest().getRequestURI()));
        log.info(String.format("$$$$$$$$$$$$$$$$$$$$$$$$$$ %s", ctx.getRouteHost()));
        if (authToken != null) {
            try {
                URL originalRouteHost = ctx.getRouteHost();
                log.info(String.format("----------- %s", userServiceUrl));
                ctx.setRouteHost(new URL("http", userServiceUrl, "user"));
                ClientHttpResponse authResponse = (ClientHttpResponse) delegateFilter.run();
                log.info("+++++++++++");
                HttpServletRequest authRequest = new HttpServletRequestWrapper();
                ctx.setRouteHost(originalRouteHost);
                log.info(ctx.get("error.status_code").toString());
                log.info(ctx.get("error.exception").toString());
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