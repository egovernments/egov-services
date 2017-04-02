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
    private final String userInfoHeader;
    private final String authServiceHost;
    private final String authUri;
    private final RestTemplate restTemplate;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper mapper;


    public AuthFilter(ProxyRequestHelper helper, RestTemplate restTemplate, String authServiceHost,
                      String authUri, String userInfoHeader) {
        this.helper = helper;
        this.restTemplate = restTemplate;
        this.authServiceHost = authServiceHost;
        this.authUri = authUri;
        this.userInfoHeader = userInfoHeader;
        mapper = new ObjectMapper();
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
        return (boolean) RequestContext.getCurrentContext().get(AUTH_BOOLEAN_FLAG_NAME);
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String authToken = (String) ctx.get(AUTH_TOKEN_KEY);
        try {
            User user = getUser(authToken);
            if (shouldPutUserInfoOnHeaders(ctx)) {
                ctx.addZuulRequestHeader(userInfoHeader, mapper.writeValueAsString(user));
            } else {
                appendUserInfoToRequestBody(ctx, user);
            }
        } catch (HttpClientErrorException ex) {
            abortWithException(ctx, ex);
        } catch (IOException ex) {
            ctx.setSendZuulResponse(false);
        }
        return null;
    }

    private User getUser(String authToken) {
        String authURL = String.format("%s%s%s", authServiceHost, authUri, authToken);
        return restTemplate.postForObject(authURL, null, User.class);
    }

    private boolean shouldPutUserInfoOnHeaders(RequestContext ctx) {
        return Objects.equals(ctx.getRequest().getMethod().toUpperCase(), GET) ||
                ctx.getRequest().getRequestURI().matches(FILESTORE_REGEX);
    }

    @SuppressWarnings("unchecked")
    private void appendUserInfoToRequestBody(RequestContext ctx, User user) throws IOException {
        HashMap<String, Object> requestBody = getRequestBody(ctx);
        HashMap<String, Object> requestInfo = (HashMap<String, Object>) requestBody.get(REQUEST_INFO_FIELD_NAME);
        requestInfo.put(USER_INFO_FIELD_NAME, user);
        requestInfo.put(CORRELATION_ID_FIELD_NAME, ctx.get(CORRELATION_ID_KEY));
        requestBody.put(REQUEST_INFO_FIELD_NAME, requestInfo);
        CustomRequestWrapper requestWrapper = new CustomRequestWrapper(ctx.getRequest());
        requestWrapper.setPayload(mapper.writeValueAsString(requestBody));
        ctx.setRequest(requestWrapper);
    }

    private HashMap<String, Object> getRequestBody(RequestContext ctx) throws IOException {
        String payload = IOUtils.toString(ctx.getRequest().getInputStream());
        return mapper.readValue(payload, new TypeReference<HashMap<String, Object>>() { });
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