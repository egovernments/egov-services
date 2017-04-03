package org.egov.filters.pre;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.io.IOUtils;
import org.egov.contract.User;
import org.egov.wrapper.CustomRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import static org.egov.constants.RequestContextConstants.*;

public class AuthFilter extends ZuulFilter {

    private final ProxyRequestHelper helper;
    private final String authServiceHost;
    private final String authUri;
    private final RestTemplate restTemplate;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    public AuthFilter(ProxyRequestHelper helper, RestTemplate restTemplate, String authServiceHost, String authUri) {
        this.helper = helper;
        this.restTemplate = restTemplate;
        this.authServiceHost = authServiceHost;
        this.authUri = authUri;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public boolean shouldFilter() {
        return RequestContext.getCurrentContext().getBoolean(AUTH_BOOLEAN_FLAG_NAME);
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String authToken = (String) ctx.get(AUTH_TOKEN_KEY);
        try {
            User user = getUser(authToken);
            ctx.set(USER_INFO_KEY, user);
        } catch (HttpClientErrorException ex) {
            abortWithException(ctx, ex);
        }
        return null;
    }

    private User getUser(String authToken) {
        String authURL = String.format("%s%s%s", authServiceHost, authUri, authToken);
        return restTemplate.postForObject(authURL, null, User.class);
    }

    private void abortWithException(RequestContext ctx, HttpClientErrorException ex) {
        ctx.setSendZuulResponse(false);
        try {
            helper.setResponse(ex.getStatusCode().value(),
					IOUtils.toInputStream(ex.getResponseBodyAsString()),
                    ex.getResponseHeaders());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}