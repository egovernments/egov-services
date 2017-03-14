package org.egov.model;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AuthRequestWrapperTest {
    private MockHttpServletRequest request = new MockHttpServletRequest();

    @Test
    public void testThatTheMethodIsAlwaysPost() {
        AuthRequestWrapper authRequestWrapper = new AuthRequestWrapper(request);
        assertThat(authRequestWrapper.getMethod(), is("POST"));
    }
}
