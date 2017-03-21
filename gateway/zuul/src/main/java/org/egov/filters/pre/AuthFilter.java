package org.egov.filters.pre;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.io.IOUtils;
import org.egov.contract.User;
import org.egov.wrapper.CustomRequestWrapper;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

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
        String authToken = (String) ctx.get("authToken");
        String authURL = String.format("%s%s%s", authServiceHost, authUri, authToken);
        try {
            User user = restTemplate.postForObject(authURL, null, User.class);
            ObjectMapper mapper = new ObjectMapper();
            if (shouldPutUserInfoOnHeaders(ctx)) {
                ctx.addZuulRequestHeader(userInfoHeader, mapper.writeValueAsString(user));
            } else {
                appendUserInfoToRequestBody(ctx, user);
            }
        } catch (HttpClientErrorException ex) {
            ex.printStackTrace();
            abortWithException(ctx, ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            ctx.setSendZuulResponse(false);
        }
        return null;
    }

    private boolean shouldPutUserInfoOnHeaders(RequestContext ctx) {
        return Objects.equals(ctx.getRequest().getMethod().toUpperCase(), "GET") ||
                ctx.getRequest().getRequestURI().matches("^/filestore/.*");
    }

    private void appendUserInfoToRequestBody(RequestContext ctx, User user) throws IOException {
        String payload = IOUtils.toString(ctx.getRequest().getInputStream());
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
        };
        HashMap<String, Object> requestBody = mapper.readValue(payload, typeRef);
        HashMap<String, Object> requestInfo = (HashMap<String, Object>) requestBody.get("RequestInfo");
        requestInfo.put("userInfo", user);
        requestBody.put("RequestInfo", requestInfo);
        CustomRequestWrapper requestWrapper = new CustomRequestWrapper(ctx.getRequest());
        requestWrapper.setPayload(mapper.writeValueAsString(requestBody));
        ctx.setRequest(requestWrapper);
    }

    private void abortWithException(RequestContext ctx, HttpClientErrorException ex) {
        ctx.setSendZuulResponse(false);
        try {
            helper.setResponse(ex.getStatusCode().value(), IOUtils.toInputStream(ex.getResponseBodyAsString()),
                    ex.getResponseHeaders());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}