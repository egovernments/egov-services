package org.egov.filters.pre;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.egov.Utils.ExceptionUtils;
import org.egov.contract.Action;
import org.egov.contract.User;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.egov.constants.RequestContextConstants.*;

/**
 *  5th pre filter to get executed.
 *  Filter gets executed if the RBAC flag is enabled. Returns an error if the URI is not present in the authorized action list.
 */
public class RbacFilter extends ZuulFilter{

    private static final String FORBIDDEN_MESSAGE = "Not authorized to access this resource";

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

        List<Action> actions = user.getActions();

        return actions.stream()
            .anyMatch(action -> isActionMatchingIncomingURI(requestUri, action));
    }

    private boolean isActionMatchingIncomingURI(String requestUri, Action action) {
        if(action.hasDynamicFields()) {
            return requestUri.matches(action.getRegexUrl());
        }
        return requestUri.equals(action.getUrl());
    }

}
