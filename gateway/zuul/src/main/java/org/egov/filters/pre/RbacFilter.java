package org.egov.filters.pre;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.egov.Utils.ExceptionUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.contract.Action;
import org.egov.contract.Role;
import org.egov.contract.User;
import org.egov.model.AuthorizationRequest;
import org.egov.model.AuthorizationRequestWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Collectors;

import static org.egov.constants.RequestContextConstants.RBAC_BOOLEAN_FLAG_NAME;
import static org.egov.constants.RequestContextConstants.USER_INFO_KEY;

/**
 *  5th pre filter to get executed.
 *  Filter gets executed if the RBAC flag is enabled. Returns an error if the URI is not present in the authorized action list.
 */
@Slf4j
public class RbacFilter extends ZuulFilter{

    private static final String FORBIDDEN_MESSAGE = "Not authorized to access this resource";

    private RestTemplate restTemplate;

    private String authorizationUrl;


    @Autowired
    public RbacFilter(RestTemplate restTemplate, String authorizationUrl) {
        this.restTemplate = restTemplate;
        this.authorizationUrl = authorizationUrl;
    }

    @Override
    public String filterType() {return "pre";}

    @Override
    public int filterOrder() {return 4;}

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return ctx.getBoolean(RBAC_BOOLEAN_FLAG_NAME);
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        final boolean isIncomingURIInAuthorizedActionList = isIncomingURIInAuthorizedActionList(ctx);
        if(isIncomingURIInAuthorizedActionList)
            return null;

        ExceptionUtils.raiseCustomException(HttpStatus.FORBIDDEN, FORBIDDEN_MESSAGE);
        return null;
    }

    private boolean isIncomingURIInAuthorizedActionList(RequestContext ctx) {
        String requestUri = ctx.getRequest().getRequestURI();
        User user = (User) ctx.get(USER_INFO_KEY);

        if (user == null) {
            ExceptionUtils.raiseCustomException(HttpStatus.UNAUTHORIZED, "User information not found. Can't execute RBAC filter");
        }

        AuthorizationRequest request = AuthorizationRequest.builder()
            .roleCodes(user.getRoles().stream().map(Role::getCode).collect(Collectors.toSet()))
            .uri(requestUri)
            .tenantId(user.getTenantId())
            .build();

        return isUriAuthorized(request);



//        List<Action> actions = user.getActions();
//
//        return actions.stream()
//            .anyMatch(action -> isActionMatchingIncomingURI(requestUri, action));
    }

    private boolean isActionMatchingIncomingURI(String requestUri, Action action) {
        if(action.hasDynamicFields()) {
            return requestUri.matches(action.getRegexUrl());
        }
        return requestUri.equals(action.getUrl());
    }

    private boolean isUriAuthorized(AuthorizationRequest authorizationRequest){
        AuthorizationRequestWrapper authorizationRequestWrapper = new AuthorizationRequestWrapper(new RequestInfo(),
            authorizationRequest);

        try {
            ResponseEntity<Void> responseEntity = restTemplate.postForEntity(authorizationUrl, authorizationRequestWrapper, Void
                .class);

            return responseEntity.getStatusCode().equals(HttpStatus.OK);
        } catch(HttpClientErrorException e){
            return false;
        } catch (Exception e){
            log.warn("Unknown exception occurred while attempting to authorize via access control", e);
            return false;
        }


    }

}
