package org.egov.pgr.model;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class SevaRequestTest {

    private static final String CORRELATION_ID = "correlationId";

    @Test
    public void test_should_retrieve_correlation_id() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final HashMap<String, Object> requestInfo = new HashMap<>();
        requestInfo.put("msg_id", CORRELATION_ID);
        sevaRequestMap.put("RequestInfo", requestInfo);
        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertEquals(CORRELATION_ID, sevaRequest.getCorrelationId());
    }

}