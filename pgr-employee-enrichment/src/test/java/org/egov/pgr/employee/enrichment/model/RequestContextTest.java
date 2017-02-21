package org.egov.pgr.employee.enrichment.model;

import org.junit.Test;
import org.slf4j.MDC;

import static org.junit.Assert.assertEquals;

public class RequestContextTest {

    @Test
    public void test_should_set_mdc_with_correlation_id() {
        RequestContext.setId("correlationId");

        assertEquals("correlationId", MDC.get(RequestContext.CORRELATION_ID));
    }

}