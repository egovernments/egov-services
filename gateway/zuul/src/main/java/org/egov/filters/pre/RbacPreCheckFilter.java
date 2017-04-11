package org.egov.filters.pre;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;

import static org.egov.constants.RequestContextConstants.RBAC_BOOLEAN_FLAG_NAME;
import static org.egov.constants.RequestContextConstants.SKIP_RBAC;

public class RbacPreCheckFilter extends ZuulFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private HashSet<String> openEndpointsWhitelist;
    private HashSet<String> anonymousEndpointsWhitelist;
    private List<String> rbacWhitelist;

    public RbacPreCheckFilter(HashSet<String> openEndpointsWhitelist,
                              HashSet<String> anonymousEndpointsWhitelist,
                              List<String> rbacWhitelist) {
        this.openEndpointsWhitelist = openEndpointsWhitelist;
        this.anonymousEndpointsWhitelist = anonymousEndpointsWhitelist;
        this.rbacWhitelist = rbacWhitelist;
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
        return true;
    }

    @Override
    public Object run() {
        if ((openEndpointsWhitelist.contains(getRequestURI())
            || anonymousEndpointsWhitelist.contains(getRequestURI()))) {
            setShouldDoRbac(false);
            logger.info(SKIP_RBAC, getRequestURI());
            return null;
        }

        //This is whitelist which has contextroot based on which we can decide which
        //modules url's should have check for RBAC
        if(!rbacWhitelist.stream().anyMatch(url -> getRequestURI().contains(url))){
            setShouldDoRbac(false);
            logger.info(SKIP_RBAC, getRequestURI());
            return null;
        }

        setShouldDoRbac(true);
        return null;
    }

    private void setShouldDoRbac(boolean enableRbac) {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.set(RBAC_BOOLEAN_FLAG_NAME, enableRbac);
    }

    private String getRequestURI() {
        return getRequest().getRequestURI();
    }

    private HttpServletRequest getRequest() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return ctx.getRequest();
    }
}
