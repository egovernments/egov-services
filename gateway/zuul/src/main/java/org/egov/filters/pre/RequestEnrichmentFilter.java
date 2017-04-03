package org.egov.filters.pre;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.io.IOUtils;
import org.egov.contract.User;
import org.egov.wrapper.CustomRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;

import static org.egov.constants.RequestContextConstants.*;

@Component
public class RequestEnrichmentFilter extends ZuulFilter {

    private final ObjectMapper objectMapper;
    private static final String CORRELATION_HEADER_NAME = "x-correlation-id";
    private static final String USER_INFO_HEADER_NAME = "x-user-info";
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public RequestEnrichmentFilter() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 3;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        modifyRequestBody();
        addRequestHeaders();
        return null;
    }

    private void addRequestHeaders() {
        RequestContext ctx = RequestContext.getCurrentContext();
        addCorrelationIdHeader(ctx);
        addUserInfoHeader(ctx);
    }

    private void addUserInfoHeader(RequestContext ctx) {
        if (isUserInfoPresent() && !isRequestBodyCompatible()) {
            User user = getUser();
            try {
                ctx.addZuulRequestHeader(USER_INFO_HEADER_NAME, objectMapper.writeValueAsString(user));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private void addCorrelationIdHeader(RequestContext ctx) {
        ctx.addZuulRequestHeader(CORRELATION_HEADER_NAME, getCorrelationId());
    }

    private void modifyRequestBody() {
        if (!isRequestBodyCompatible()) {
            return;
        }
        try {
            enrichRequestBody();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isRequestBodyCompatible() {
        return POST.equalsIgnoreCase(getRequestMethod()) && !getRequestURI().matches(FILESTORE_REGEX);
    }

    private HttpServletRequest getRequest() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return ctx.getRequest();
    }

    private String getRequestMethod() {
        return getRequest().getMethod();
    }

    private String getRequestURI() {
        return getRequest().getRequestURI();
    }

    @SuppressWarnings("unchecked")
    private void enrichRequestBody() throws IOException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HashMap<String, Object> requestBody = getRequestBody(ctx);
        HashMap<String, Object> requestInfo = (HashMap<String, Object>) requestBody.get(REQUEST_INFO_FIELD_NAME);
        if (requestInfo == null) {
            return;
        }
        setUserInfo(requestInfo);
        setCorrelationId(requestInfo);
        requestBody.put(REQUEST_INFO_FIELD_NAME, requestInfo);
        CustomRequestWrapper requestWrapper = new CustomRequestWrapper(ctx.getRequest());
        requestWrapper.setPayload(objectMapper.writeValueAsString(requestBody));
        ctx.setRequest(requestWrapper);
    }

    private void setCorrelationId(HashMap<String, Object> requestInfo) {
        requestInfo.put(CORRELATION_ID_FIELD_NAME, getCorrelationId());
    }

    private String getCorrelationId() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return (String) ctx.get(CORRELATION_ID_KEY);
    }

    private void setUserInfo(HashMap<String, Object> requestInfo) {
        if (isUserInfoPresent()) {
            requestInfo.put(USER_INFO_FIELD_NAME, getUser());
        }
    }

    private User getUser() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return (User) ctx.get(USER_INFO_KEY);
    }

    private boolean isUserInfoPresent() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return ctx.get(USER_INFO_KEY) != null;
    }

    private HashMap<String, Object> getRequestBody(RequestContext ctx) throws IOException {
        String payload = IOUtils.toString(ctx.getRequest().getInputStream());
        return objectMapper.readValue(payload, new TypeReference<HashMap<String, Object>>() {
        });
    }

}