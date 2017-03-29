package org.egov.pgr.contracts.grievance;

import org.junit.Test;

import java.util.Date;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class ServiceRequestTest {

    @Test
    public void test_should_return_escalation_date_from_service_request_map() {
        final HashMap<String, Object> serviceRequestMap = new HashMap<>();
        final Date expectedDate = new Date();
        serviceRequestMap.put("expected_datetime", expectedDate);
        final ServiceRequest serviceRequest = new ServiceRequest(serviceRequestMap);

        final Date actualEscalationDate = serviceRequest.getEscalationDate();

        assertEquals(expectedDate, actualEscalationDate);
    }

}