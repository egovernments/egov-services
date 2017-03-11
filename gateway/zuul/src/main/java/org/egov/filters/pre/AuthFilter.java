package org.egov.filters.pre;


import com.netflix.client.ClientException;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.egov.model.AuthRequest;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

@Component
public class AuthFilter extends ZuulFilter {
    private static Logger log = LoggerFactory.getLogger(AuthFilter.class);

    private ProxyRequestHelper helper = new ProxyRequestHelper();
    private RibbonRoutingFilter delegateFilter;
    private List<RibbonRequestCustomizer> requestCustomizers = new ArrayList<>();

    @Autowired
    RibbonCommandFactory<?> ribbonCommandFactory;

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
        return !RequestContext.getCurrentContext().getRequest().getRequestURI().contains("/user");
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String authToken = ctx.getRequest().getHeader("auth_token");
        if (authToken != null) {
            try {
                String originalServiceId = (String) ctx.get("serviceId");
                HashMap<String, List<String>> originalRequestQueryParms = (HashMap<String, List<String>>) ctx.getRequestQueryParams();
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

                ctx.set("serviceId", originalServiceId);
                ctx.setRequestQueryParams(originalRequestQueryParms);
                ctx.set("requestURI", originalRequestUri);
                ctx.setRequest(request);

                if (isAuthenticationSuccessful(authResponse)) {
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

    private boolean isAuthenticationSuccessful(ClientHttpResponse response) throws IOException {
        return response != null && response.getStatusCode().is2xxSuccessful();
    }

    private void setResponse(ClientHttpResponse resp) throws ClientException, IOException {
        helper.setResponse(resp.getStatusCode().value(),
                resp.getBody() == null ? null : resp.getBody(), resp.getHeaders());
    }

}