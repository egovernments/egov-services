package org.egov.filters.pre;

import com.netflix.zuul.context.RequestContext;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class CorrelationIdFilterTest {

	private CorrelationIdFilter correlationIdFilter;

	@Before
	public void before() {
		correlationIdFilter = new CorrelationIdFilter();
	}

	@Test
	public void test_should_set_custom_correlation_id_header() {
		correlationIdFilter.run();

		final Map<String, String> zuulRequestHeaders = RequestContext.getCurrentContext().getZuulRequestHeaders();
		assertNotNull(zuulRequestHeaders.get("x-correlation-id"));
	}

	@Test
	public void test_should_set_filter_order_to_beginning() {
		assertEquals(0, correlationIdFilter.filterOrder());
	}

	@Test
	public void test_should_execute_as_pre_filter() {
		assertEquals("pre", correlationIdFilter.filterType());
	}

	@Test
	public void test_should_always_execute_filter() {
		assertTrue( correlationIdFilter.shouldFilter());
	}

}