package org.egov.filters.pre;

import com.netflix.zuul.context.RequestContext;
import org.egov.filters.route.AuthFilter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonRoutingFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AuthFilterTest {
    private MockHttpServletRequest request = new MockHttpServletRequest();

    @Mock
    private RibbonRoutingFilter delegateFilter;

    private AuthFilter authFilter;

    @Before
    public void init() {
        initMocks(this);
        authFilter = new AuthFilter(delegateFilter);
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.clear();
        ctx.setRequest(request);
    }

    @Test
    public void testBasicProperties() {
        when(delegateFilter.filterOrder()).thenReturn(1);

        assertThat(authFilter.filterType(), is("route"));
        assertThat(authFilter.filterOrder(), is(1));
    }

    @Test
    public void testThatFilterShouldNotBeAppliedForUserApi() {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/user/_login");
        RequestContext.getCurrentContext().setRequest(request);
        assertFalse(authFilter.shouldFilter());
    }

    @Test
    public void testThatFilterShouldNotBeAppliedWhenNoAuthTokenInHeader() {
        RequestContext.getCurrentContext().setRequest(request);
        assertFalse(authFilter.shouldFilter());
    }

    @Test
    public void testThatFilterShouldApplyForNonUserApisWithAuthToken() {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/pgr");
        request.addHeader("auth_token", "fubar");
        RequestContext.getCurrentContext().setRequest(request);
        assertTrue(authFilter.shouldFilter());
    }

    @Test
    public void testThatFilterSetsUserInfoInHeaderAfterValidationAuthToken() throws IOException {
        RequestContext ctx = RequestContext.getCurrentContext();
        ClientHttpResponse response = mock(ClientHttpResponse.class);
        ctx.setRouteHost(new URL("http", "localhost", 8080, "/"));
        ctx.setRequestQueryParams(new HashMap<>());
        String responseBody = "{\"id\":30,\"userName\":\"malathi\",\"name\":\"malathi\",\"mobileNumber\":\"9686602042\",\"emailId\":\"fu@bar.com\",\"locale\":\"en_IN\",\"type\":\"EMPLOYEE\",\"roles\":[{\"id\":15,\"name\":\"Employee\",\"description\":\"Default role for all employees\",\"version\":0,\"createdDate\":1440720000000,\"lastModifiedDate\":1440720000000,\"new\":false},{\"id\":16,\"name\":\"ULB Operator\",\"description\":\"ULB Operator\",\"version\":0,\"createdDate\":1440758717567,\"lastModifiedDate\":1440758717567,\"new\":false}],\"active\":true}";
        InputStream is = new ByteArrayInputStream(responseBody.getBytes());

        when(response.getBody()).thenReturn(is);
        when(response.getStatusCode()).thenReturn(HttpStatus.OK);
        when(delegateFilter.run()).thenReturn(response).thenReturn(null);
        authFilter.run();
        System.out.println(ctx.get("error.status_code"));
        assertThat(ctx.getZuulRequestHeaders().get("user_info"), is("{\"id\":30,\"userName\":\"malathi\",\"name\":\"malathi\",\"type\":\"EMPLOYEE\",\"mobileNumber\":\"9686602042\",\"emailId\":\"fu@bar.com\",\"roles\":[{\"name\":\"Employee\"},{\"name\":\"ULB Operator\"}]}"));
    }

    @Test
    public void testThatFilterAbortsOnInvalidAuthToken() throws IOException {
        RequestContext ctx = RequestContext.getCurrentContext();
        ClientHttpResponse response = mock(ClientHttpResponse.class);
        ctx.setRouteHost(new URL("http", "localhost", 8080, "/"));
        ctx.setRequestQueryParams(new HashMap<>());
        ctx.setResponse(new MockHttpServletResponse());

        when(response.getBody()).thenReturn(null);
        when(response.getStatusCode()).thenReturn(HttpStatus.UNAUTHORIZED);
        when(response.getHeaders()).thenReturn(new HttpHeaders());
        when(delegateFilter.run()).thenReturn(response);
        authFilter.run();
        assertThat(ctx.getResponse().getStatus(), is(401));
        assertFalse(ctx.sendZuulResponse());
        assertNull(ctx.getZuulRequestHeaders().get("requester_id"));
    }
}
