package org.egov.filters.pre;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.io.IOUtils;
import org.egov.Resources;
import org.egov.contract.Role;
import org.egov.contract.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthFilterTest {
    private MockHttpServletRequest request = new MockHttpServletRequest();
    private Resources resources = new Resources();

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ProxyRequestHelper proxyRequestHelper;

    private AuthFilter authFilter;

    private String authServiceHost = "http://localhost:8082/";
    private String authUri = "user/_details?access_token=";
    private String userInfoHeader = "x-user-info";

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        authFilter = new AuthFilter(proxyRequestHelper, restTemplate, authServiceHost, authUri, userInfoHeader);
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.clear();
        ctx.setRequest(request);
    }

    @Test
    public void testBasicProperties() {
        assertThat(authFilter.filterType(), is("pre"));
        assertThat(authFilter.filterOrder(), is(2));
    }

    @Test
    public void testThatFilterShouldBeAppliedBasedOnContext() {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.set("shouldDoAuth", false);
        assertFalse(authFilter.shouldFilter());

        ctx.set("shouldDoAuth", true);
        assertTrue(authFilter.shouldFilter());
    }

    @Test
    public void testThatFilterShouldSetUserInfoInHeaderAfterValidatingAuthTokenForGETRequests() throws IOException {
        String authToken = "dummy-auth-token";
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.set("authToken", authToken);
        ctx.set("CORRELATION_ID", "123-123-123-123");
        request.setMethod("GET");
        ctx.setRequest(request);

        String authUrl = String.format("%s%s%s", authServiceHost, authUri, authToken);
        User mockUser = new User();
        mockUser.setId(30);
        mockUser.setUserName("userName");
        mockUser.setName("name");
        mockUser.setMobileNumber("1234567890");
        mockUser.setEmailId("fu@bar.com");
        mockUser.setType("EMPLOYEE");

        Role mockRole1 = new Role();
        mockRole1.setName("Employee");

        Role mockRole2 = new Role();
        mockRole2.setName("ULB Operator");

        List<Role> roles = new ArrayList<>();
        roles.add(mockRole1);
        roles.add(mockRole2);

        mockUser.setRoles(roles);

        when(restTemplate.postForObject(authUrl, null, User.class)).thenReturn(mockUser);

        authFilter.run();
        assertThat(ctx.getZuulRequestHeaders().get(userInfoHeader),
            is("{\"id\":30,\"userName\":\"userName\",\"name\":\"name\",\"type\":\"EMPLOYEE\",\"mobileNumber\":\"1234567890\",\"emailId\":\"fu@bar.com\",\"roles\":[{\"name\":\"Employee\"},{\"name\":\"ULB Operator\"}]}"));
        assertTrue(ctx.sendZuulResponse());
    }

    @Test
    public void testThatFilterShouldSetUserInfoInHeaderAfterValidatingAuthTokenForFileStore() throws IOException {
        String authToken = "dummy-auth-token";
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.set("authToken", authToken);
        request.setMethod("POST");
        request.setRequestURI("/filestore/v1/files");
        ctx.setRequest(request);

        String authUrl = String.format("%s%s%s", authServiceHost, authUri, authToken);
        User mockUser = new User();
        mockUser.setId(30);
        mockUser.setUserName("userName");
        mockUser.setName("name");
        mockUser.setMobileNumber("1234567890");
        mockUser.setEmailId("fu@bar.com");
        mockUser.setType("EMPLOYEE");

        Role mockRole1 = new Role();
        mockRole1.setName("Employee");

        Role mockRole2 = new Role();
        mockRole2.setName("ULB Operator");

        List<Role> roles = new ArrayList<>();
        roles.add(mockRole1);
        roles.add(mockRole2);

        mockUser.setRoles(roles);

        when(restTemplate.postForObject(authUrl, null, User.class)).thenReturn(mockUser);

        authFilter.run();
        assertThat(ctx.getZuulRequestHeaders().get(userInfoHeader),
            is("{\"id\":30,\"userName\":\"userName\",\"name\":\"name\",\"type\":\"EMPLOYEE\",\"mobileNumber\":\"1234567890\",\"emailId\":\"fu@bar.com\",\"roles\":[{\"name\":\"Employee\"},{\"name\":\"ULB Operator\"}]}"));
        assertTrue(ctx.sendZuulResponse());
    }

    @Test
    public void testThatFilterShouldSetUserInfoInRequestBodyAfterValidatingAuthTokenForPOSTRequests() throws IOException {
        String authToken = "dummy-auth-token";
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.set("authToken", authToken);
        ctx.set("CORRELATION_ID", "123-123-123-123");
        request.setMethod("POST");
        request.setContent(IOUtils.toByteArray(IOUtils.toInputStream("{\"RequestInfo\": {\"fu\": \"bar\", \"authToken\": \"dummy-auth-token\"}}")));
        ctx.setRequest(request);
        String authUrl = String.format("%s%s%s", authServiceHost, authUri, authToken);
        User mockUser = new User();
        mockUser.setId(30);
        mockUser.setUserName("userName");
        mockUser.setName("name");
        mockUser.setMobileNumber("1234567890");
        mockUser.setEmailId("fu@bar.com");
        mockUser.setType("EMPLOYEE");
        Role mockRole1 = new Role();
        mockRole1.setName("Employee");
        Role mockRole2 = new Role();
        mockRole2.setName("ULB Operator");
        List<Role> roles = new ArrayList<>();
        roles.add(mockRole1);
        roles.add(mockRole2);
        mockUser.setRoles(roles);
        when(restTemplate.postForObject(authUrl, null, User.class)).thenReturn(mockUser);

        authFilter.run();

        String expectedBody = resources.getFileContents("requestInfoWithUserInfo.json");
        assertEquals(expectedBody, IOUtils.toString(ctx.getRequest().getInputStream()));
        int expectedContentLength = 273;
        assertEquals(expectedContentLength, ctx.getRequest().getContentLength());
        assertEquals(expectedContentLength, ctx.getRequest().getContentLengthLong());
        assertEquals(null, ctx.getZuulRequestHeaders().get(userInfoHeader));
        assertTrue(ctx.sendZuulResponse());
    }

    @Test
    public void testThatFilterShouldAbortIfValidatingAuthTokenFails() throws IOException {
        RequestContext ctx = RequestContext.getCurrentContext();
        String authToken = "dummy-auth-token";
        ctx.set("authToken", authToken);
        request.setMethod("POST");
        ctx.setRequest(request);
        ctx.setResponse(new MockHttpServletResponse());
        String authUrl = String.format("%s%s%s", authServiceHost, authUri, authToken);
        when(restTemplate.postForObject(authUrl, null, User.class))
            .thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED));

        authFilter.run();
        assertFalse(ctx.sendZuulResponse());
        verify(proxyRequestHelper).setResponse(eq(401), any(), any());
    }

    @Test
    public void testThatFilterShouldAbortIfCannotParseAuthResponse() throws IOException {
        RequestContext ctx = RequestContext.getCurrentContext();
        String authToken = "dummy-auth-token";
        ctx.set("authToken", authToken);
        request.setMethod("POST");
        ctx.setRequest(request);
        ctx.setResponse(new MockHttpServletResponse());
        String authUrl = String.format("%s%s%s", authServiceHost, authUri, authToken);
        when(restTemplate.postForObject(authUrl, null, User.class)).thenThrow(JsonProcessingException.class);

        authFilter.run();

        assertFalse(ctx.sendZuulResponse());
    }
}
