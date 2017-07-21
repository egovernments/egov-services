package org.egov.tracer.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.Resources;
import org.egov.tracer.config.ObjectMapperFactory;
import org.egov.tracer.model.RequestContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CorrelationIdFilterTest {

    private static final String CORRELATION_ID = "someCorrelationId";
    private static final String GET = "GET";
    private static final String POST = "POST";
    private CorrelationIdFilter correlationIdFilter;
    private final Resources resources = new Resources();

    @Mock
    private FilterChain filterChain;

    @Mock
    private ObjectMapperFactory objectMapperFactory;

    @Before
    public void before() {
        when(objectMapperFactory.create()).thenReturn(new ObjectMapper());
        correlationIdFilter = new CorrelationIdFilter(true, objectMapperFactory);
    }

    @Test
    public void test_should_set_correlation_id_taken_from_custom_header_to_context_for_GET_request()
        throws IOException, ServletException {
        final MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        final MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        servletRequest.setMethod(GET);
        servletRequest.addHeader("x-correlation-id", CORRELATION_ID);

        correlationIdFilter.doFilter(servletRequest, servletResponse, filterChain);

        assertEquals(CORRELATION_ID, RequestContext.getId());
    }

    @Test
    public void test_should_set_correlation_id_taken_from_custom_header_for_POST_request_with_multi_part_content_type()
        throws IOException, ServletException {
        final MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        final MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        servletRequest.setMethod(POST);
        servletRequest.addHeader("x-correlation-id", CORRELATION_ID);
        servletRequest.setContentType("multipart/form-data");

        correlationIdFilter.doFilter(servletRequest, servletResponse, filterChain);

        assertEquals(CORRELATION_ID, RequestContext.getId());
    }

    @Test
    public void test_should_set_random_correlation_id_to_context_for_GET_request_with_no_custom_header()
        throws IOException, ServletException {
        final MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        final MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        servletRequest.setMethod(GET);

        correlationIdFilter.doFilter(servletRequest, servletResponse, filterChain);

        assertNotNull(RequestContext.getId());
    }

    @Test
    public void test_should_set_correlation_id_taken_from_request_body_having_correlation_id_field()
        throws IOException, ServletException {
        final MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        final MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        servletRequest.setMethod(POST);
        servletRequest.setContentType("application/json");
        servletRequest.setContent(resources.getFileContents("requestBodyWithCorrelationIdPresentInRequestInfo.json").getBytes());

        correlationIdFilter.doFilter(servletRequest, servletResponse, filterChain);

        assertEquals(CORRELATION_ID, RequestContext.getId());
    }

    @Test
    public void test_should_set_random_correlation_id_when_request_body_does_not_have_correlation_id_field()
        throws IOException, ServletException {
        final MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        final MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        servletRequest.setMethod(POST);
        servletRequest.setContentType("application/json");
        servletRequest.setContent(resources.getFileContents("requestBodyWithoutCorrelationIdPresentInRequestInfo.json").getBytes());

        correlationIdFilter.doFilter(servletRequest, servletResponse, filterChain);

        assertNotNull(RequestContext.getId());
    }

    @Test
    public void test_should_set_random_correlation_id_when_request_body_does_not_have_request_info_field()
        throws IOException, ServletException {
        final MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        final MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        servletRequest.setMethod(POST);
        servletRequest.setContentType("application/json");
        servletRequest.setContent(resources.getFileContents("requestBodyWithoutRequestInfoField.json").getBytes());

        correlationIdFilter.doFilter(servletRequest, servletResponse, filterChain);

        assertNotNull(RequestContext.getId());
    }

    @Test
    public void test_should_set_correlation_id_from_custom_header_when_present_and_request_body_does_not_have_request_info_field()
        throws IOException, ServletException {
        final MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        final MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        servletRequest.setMethod(POST);
        servletRequest.setContentType("application/json");
        servletRequest.addHeader("x-correlation-id", CORRELATION_ID);
        servletRequest.setContent(resources.getFileContents("requestBodyWithoutRequestInfoField.json").getBytes());

        correlationIdFilter.doFilter(servletRequest, servletResponse, filterChain);

        assertEquals(CORRELATION_ID, RequestContext.getId());
    }


}