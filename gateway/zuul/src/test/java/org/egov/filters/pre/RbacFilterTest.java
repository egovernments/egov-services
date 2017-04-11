package org.egov.filters.pre;

import com.netflix.zuul.context.RequestContext;
import org.egov.contract.Action;
import org.egov.contract.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.egov.constants.RequestContextConstants.ERROR_CODE_KEY;
import static org.egov.constants.RequestContextConstants.USER_INFO_KEY;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class RbacFilterTest {
    private MockHttpServletRequest request = new MockHttpServletRequest();

    private RbacFilter rbacFilter;
    private List<String> rbacWhitelist = new ArrayList<>(Arrays.asList("/pgr"));

    @Before
    public void init(){
        rbacFilter = new RbacFilter(rbacWhitelist);
    }

    @Test
    public void testThatFilterOrderIs3() throws Exception {
        assertThat(rbacFilter.filterOrder(), is(4));
    }

    @Test
    public void testThatFilterShouldNotRunWhenRbacIsNotRequired() throws Exception {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.set("shouldDoRbac", false);
        assertFalse(rbacFilter.shouldFilter());
    }

    @Test
    public void testThatFilterShouldNotRunWhenRequestUriIsNotInRBACWhitelist() throws Exception {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.set("shouldDoRbac", true);
        request.setRequestURI("/hr-masters/do/something");
        ctx.setRequest(request);
        assertFalse(rbacFilter.shouldFilter());
    }

    @Test
    public void shouldAbortWhenUserIsRequestingUnauthorizedURI() throws Exception {
        User user = new User();
        Action action1  = new Action();
        action1.setUrl("/pgr/seva");
        user.setActions(new ArrayList<>(Arrays.asList(action1)));
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.set(USER_INFO_KEY, user);

        request.setRequestURI("/hr-masters/do/something");
        ctx.setRequest(request);
        rbacFilter.run();

        assertEquals(404, ctx.get(ERROR_CODE_KEY));
        assertFalse(ctx.sendZuulResponse());
    }

    @Test
    public void shouldNotAbortWhenUserIsRequestingAuthorizedURI() throws Exception {
        User user = new User();
        Action action1  = new Action();
        action1.setUrl("/pgr/seva");
        user.setActions(new ArrayList<>(Arrays.asList(action1)));
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.set(USER_INFO_KEY, user);

        request.setRequestURI("/pgr/seva");
        ctx.setRequest(request);
        rbacFilter.run();

        assertEquals(null, ctx.get(ERROR_CODE_KEY));
    }

    @Test
    public void shouldAbortWhenUserDoesNotHaveAnyAuthorizedURI() throws Exception {
        RequestContext ctx = RequestContext.getCurrentContext();
        request.setRequestURI("/hr-masters/do/something");
        ctx.setRequest(request);
        rbacFilter.run();
        User user = new User();

        user.setActions(new ArrayList<>());
        ctx.set(USER_INFO_KEY, user);

        assertEquals(404, ctx.get(ERROR_CODE_KEY));
        assertFalse(ctx.sendZuulResponse());
    }

}