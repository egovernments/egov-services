package org.egov.filters.pre;


import com.netflix.config.DynamicBooleanProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.constants.ZuulConstants;
import com.netflix.zuul.context.RequestContext;
import org.egov.model.AuthenticatedUser;
import org.egov.persistence.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public class AuthFilter extends ZuulFilter {
    @Autowired
    private UserRepository userRepository;
    private static Logger log = LoggerFactory.getLogger(AuthFilter.class);

    private static final DynamicBooleanProperty routingDebug = DynamicPropertyFactory.getInstance().getBooleanProperty(ZuulConstants.ZUUL_DEBUG_REQUEST, true);
    private static final DynamicStringProperty debugParameter = DynamicPropertyFactory.getInstance().getStringProperty(ZuulConstants.ZUUL_DEBUG_PARAMETER, "d");

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    public boolean shouldFilter() {
        return "true".equals(RequestContext.getCurrentContext().getRequest().getParameter(debugParameter.get())) || routingDebug.get();

    }

    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.info(String.format("%s fuuuuubaaarrreedd  request to %s", request.getMethod(), request.getRequestURL().toString()));
        String authToken = ctx.getZuulRequestHeaders().get("auth_token");
        log.info(String.format("$$$$$$$$$$$$$$$$$$$$$$$$$$ %s", authToken));
        if (authToken != null) {
            AuthenticatedUser user = userRepository.findUser(authToken);
            if (user != null) {
                log.info(String.format("%s fuuuuubaaarrreedd user: %s authenticated", request.getMethod(), user.getName()));
            } else {
                log.info(String.format("%s fuuuuubaaarrreedd not authenticated", request.getMethod()));
            }
        }

        return null;
    }
}