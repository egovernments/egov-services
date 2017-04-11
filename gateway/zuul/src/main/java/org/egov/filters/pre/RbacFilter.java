package org.egov.filters.pre;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.egov.contract.User;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.egov.constants.RequestContextConstants.*;

public class RbacFilter extends ZuulFilter{

    private List<String> rbacWhitelist;
    public RbacFilter(List<String> rbacWhitelist){
        this.rbacWhitelist = rbacWhitelist;
    }

    @Override
    public String filterType() {return "pre";}

    @Override
    public int filterOrder() {return 4;}

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return ctx.getBoolean("shoulDoRbac");
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String requestUri = ctx.getRequest().getRequestURI();
        User user = (User) ctx.get(USER_INFO_KEY);
        if(user.getActions().stream().anyMatch(action -> requestUri.equals(action.getUrl())))
            return null;

        abortWithStatus(ctx,HttpStatus.NOT_FOUND,"The resource you are trying to find is not available");
        return null;
    }


    private void abortWithStatus(RequestContext ctx, HttpStatus status, String message) {
        ctx.set(ERROR_CODE_KEY, status.value());
        ctx.set(ERROR_MESSAGE_KEY, message);
        ctx.setSendZuulResponse(false);
    }
}
