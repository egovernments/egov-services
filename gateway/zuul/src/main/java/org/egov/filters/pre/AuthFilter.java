package org.egov.filters.pre;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.io.IOUtils;
import org.egov.contract.User;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class AuthFilter extends ZuulFilter {

    private ProxyRequestHelper helper = new ProxyRequestHelper();
    private String userInfoHeader;
    private String authServiceHost;
    private String authUri;
    private RestTemplate restTemplate;

    public AuthFilter(RestTemplate restTemplate, String authServiceHost, String authUri, String userInfoHeader) {
        this.restTemplate = restTemplate;
        this.authServiceHost = authServiceHost;
        this.authUri = authUri;
        this.userInfoHeader = userInfoHeader;
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
        return (boolean) RequestContext.getCurrentContext().get("shouldDoAuth");
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String authToken = ctx.getRequest().getHeader("auth_token");
        String authURL = String.format("%s%s%s", authServiceHost, authUri, authToken);
        try {
            User user = restTemplate.postForObject(authURL, null, User.class);
            ObjectMapper mapper = new ObjectMapper();
            ctx.addZuulRequestHeader(userInfoHeader, mapper.writeValueAsString(user));
        } catch (HttpClientErrorException ex) {
            ex.printStackTrace();
            abortWithException(ctx, ex);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            ctx.setSendZuulResponse(false);
        }
        return null;
    }

    private void abortWithException(RequestContext ctx, HttpClientErrorException ex) {
        ctx.setSendZuulResponse(false);
        try {
            helper.setResponse(ex.getStatusCode().value(), IOUtils.toInputStream(ex.getResponseBodyAsString()), ex.getResponseHeaders());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}