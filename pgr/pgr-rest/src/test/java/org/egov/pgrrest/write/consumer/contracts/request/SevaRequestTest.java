package org.egov.pgrrest.write.consumer.contracts.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.pgrrest.common.domain.model.AttributeEntry;
import org.egov.pgrrest.write.domain.model.ServiceRequestRecord;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SevaRequestTest {

    private static final String IST = "Asia/Calcutta";
    private ObjectMapper objectMapper;

    @Before
    public void before() {
        TimeZone.setDefault(TimeZone.getTimeZone(IST));
        objectMapper = new ObjectMapper();
        objectMapper.setTimeZone(TimeZone.getTimeZone(IST));
    }

    @Test
    public void test_should_convert_from_seva_request_map_to_domain_complaint_record() {
        final HashMap<String, Object> sevaRequestMap = SevaRequestMapFactory.create();

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap, objectMapper);

        final ServiceRequestRecord serviceRequestRecord = sevaRequest.toDomain();

        assertNotNull(serviceRequestRecord);
        assertEquals("crn", serviceRequestRecord.getCRN());
        assertEquals(Long.valueOf(29), serviceRequestRecord.getDepartment());
        assertEquals(Long.valueOf(3), serviceRequestRecord.getCreatedBy());
        assertEquals(Long.valueOf(3), serviceRequestRecord.getLastModifiedBy());
        assertEquals(0.0d, serviceRequestRecord.getLatitude(), 0);
        assertEquals(0.0d, serviceRequestRecord.getLongitude(), 0);
        assertEquals("complaint description", serviceRequestRecord.getDescription());
        assertEquals("landmark", serviceRequestRecord.getLandmarkDetails());
        assertEquals("firstName", serviceRequestRecord.getRequesterName());
        assertEquals("1234567890", serviceRequestRecord.getRequesterMobileNumber());
        assertEquals("email@email.com", serviceRequestRecord.getRequesterEmail());
        assertEquals("complainant address", serviceRequestRecord.getRequesterAddress());
        assertEquals("complaintTypeCode", serviceRequestRecord.getServiceRequestTypeCode());
        assertEquals(Long.valueOf(6), serviceRequestRecord.getPositionId());
        assertEquals("REGISTERED", serviceRequestRecord.getServiceRequestStatus());
        assertEquals("ap.public", serviceRequestRecord.getTenantId());
        final List<AttributeEntry> actualAttributeEntries = serviceRequestRecord.getAttributeEntries();
        assertEquals(10, actualAttributeEntries.size());
    }

    private Date toDate(LocalDateTime localDateTime) {
        return new org.egov.pgr.common.date.Date(localDateTime).toDate();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test_should_set_complainant_user_id_when_logged_in_citizen_creates_complaint() {
        final HashMap<String, Object> sevaRequestMap = SevaRequestMapFactory.create();
        final HashMap<String, Object> requestInfo = (HashMap<String, Object>) sevaRequestMap.get("RequestInfo");
        final HashMap<String, Object> userInfo = (HashMap<String, Object>) requestInfo.get("userInfo");
        userInfo.put("type", "CITIZEN");
        userInfo.put("id", "4");
        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap, objectMapper);

        final ServiceRequestRecord serviceRequestRecord = sevaRequest.toDomain();

        assertNotNull(serviceRequestRecord);
        assertEquals(Long.valueOf(4), serviceRequestRecord.getLoggedInRequesterUserId());
    }

}

