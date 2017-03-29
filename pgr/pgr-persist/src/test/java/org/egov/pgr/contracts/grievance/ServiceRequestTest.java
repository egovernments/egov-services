package org.egov.pgr.contracts.grievance;

import org.apache.commons.lang3.time.DateUtils;
import org.egov.pgr.DateConverter;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static org.junit.Assert.assertTrue;

public class ServiceRequestTest {

    @Test
    public void test_should_return_escalation_date_from_service_request_map() {
        final HashMap<String, Object> serviceRequestMap = new HashMap<>();
        serviceRequestMap.put("expected_datetime", 1490949323177L);
        final ServiceRequest serviceRequest = new ServiceRequest(serviceRequestMap);

        final Date actualEscalationDate = serviceRequest.getEscalationDate();

        final LocalDateTime expectedDateTime = LocalDateTime.of(2017, 3, 31, 14, 5, 23);
        final Date expectedDate = new DateConverter(expectedDateTime).toDate();
        final String message = String.format("Expected: %s, Actual: %s", expectedDate, actualEscalationDate);
        assertTrue(message, DateUtils.truncatedEquals(expectedDate, actualEscalationDate, Calendar.SECOND));
    }

}