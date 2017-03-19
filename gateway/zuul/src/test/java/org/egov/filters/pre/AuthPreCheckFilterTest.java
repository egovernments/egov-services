package org.egov.filters.pre;

import com.netflix.zuul.context.RequestContext;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class AuthPreCheckFilterTest {
    private MockHttpServletRequest request = new MockHttpServletRequest();

    private AuthPreCheckFilter authPreCheckFilter;

    @Before
    public void init() {
        authPreCheckFilter = new AuthPreCheckFilter("/open-endpoint1,/open-endpoint2",
                "anonymous-endpoint1,anonymous-endpoint2");
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.clear();
        ctx.setRequest(request);
    }

    @Test
    public void testBasicProperties() {
        assertThat(authPreCheckFilter.filterType(), is("pre"));
        assertThat(authPreCheckFilter.filterOrder(), is(1));
        assertTrue(authPreCheckFilter.shouldFilter());
    }

    @Test
    public void testThatAuthShouldNotHappenForOpenEndpoints() {
        RequestContext ctx = RequestContext.getCurrentContext();
        request.setRequestURI("/open-endpoint1");
        ctx.setRequest(request);
        authPreCheckFilter.run();
        assertFalse((Boolean) ctx.get("shouldDoAuth"));

        request.setRequestURI("/open-endpoint2");
        ctx.setRequest(request);
        authPreCheckFilter.run();
        assertFalse((Boolean) ctx.get("shouldDoAuth"));
    }

    @Test
    public void testThatAuthShouldNotHappenForAnonymousEndpointsOnNoAuthToken() {
        RequestContext ctx = RequestContext.getCurrentContext();
        request.setRequestURI("/anonymous-endpoint1");
        ctx.setRequest(request);
        authPreCheckFilter.run();
        assertFalse((Boolean) ctx.get("shouldDoAuth"));

        request.setRequestURI("/anonymous-endpoint1");
        ctx.setRequest(request);
        authPreCheckFilter.run();
        assertFalse((Boolean) ctx.get("shouldDoAuth"));
    }

    @Test
    public void testThatAuthShouldHappenForAnonymousEndpointsOnAuthToken() {
        RequestContext ctx = RequestContext.getCurrentContext();
        request.addHeader("auth_token", "token");

        request.setRequestURI("/anonymous-endpoint1");
        ctx.setRequest(request);
        authPreCheckFilter.run();
        assertTrue((Boolean) ctx.get("shouldDoAuth"));

        request.setRequestURI("/anonymous-endpoint1");
        ctx.setRequest(request);
        authPreCheckFilter.run();
        assertTrue((Boolean) ctx.get("shouldDoAuth"));
    }

    @Test
    public void testThatAuthShouldHappenForOtherEndpointsOnAuthToken() {
        RequestContext ctx = RequestContext.getCurrentContext();
        request.addHeader("auth_token", "token");

        request.setRequestURI("/other-endpoint");
        ctx.setRequest(request);
        authPreCheckFilter.run();
        assertTrue((Boolean) ctx.get("shouldDoAuth"));
    }

    @Test
    public void testThatFilterShouldAbortForOtherEndpointsOnNoAuthToken() {
        RequestContext ctx = RequestContext.getCurrentContext();

        request.setRequestURI("/other-endpoint");
        ctx.setRequest(request);
        authPreCheckFilter.run();
        assertFalse(ctx.sendZuulResponse());
        assertThat(ctx.get("error.status_code"), is(401));
    }
}
