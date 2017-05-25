package org.egov.pgrrest.write.contracts.grievance;

import org.egov.pgrrest.common.model.AttributeEntry;
import org.egov.pgrrest.write.model.ServiceRequestRecord;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class SevaRequestTest {

    @Test
    public void test_should_convert_from_seva_request_map_to_domain_complaint_record() {
        final HashMap<String, Object> sevaRequestMap = SevaRequestMapFactory.create();
        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        final ServiceRequestRecord serviceRequestRecord = sevaRequest.toDomain();

        assertNotNull(serviceRequestRecord);
        assertEquals("crn", serviceRequestRecord.getCRN());
        final Date expectedEscalationDate = toDate(LocalDateTime.of(2017, 4, 1, 13, 20, 47));
        assertEquals(expectedEscalationDate, serviceRequestRecord.getEscalationDate());
        final Date expectedLastModifiedDate = toDate(LocalDateTime.of(2017, 4, 2, 13, 20, 47));
        assertEquals(expectedLastModifiedDate, serviceRequestRecord.getLastModifiedDate());
        final Date expectedCreatedDate = toDate(LocalDateTime.of(2017, 4, 1, 13, 20, 47));
        assertEquals(expectedCreatedDate, serviceRequestRecord.getCreatedDate());
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
        assertEquals("receiving mode", serviceRequestRecord.getReceivingMode());
        assertEquals(Long.valueOf(5), serviceRequestRecord.getReceivingCenter());
        assertEquals("complaintTypeCode", serviceRequestRecord.getServiceRequestTypeCode());
        assertEquals(Long.valueOf(6), serviceRequestRecord.getAssigneeId());
        assertEquals("REGISTERED", serviceRequestRecord.getServiceRequestStatus());
        assertEquals(Long.valueOf(7), serviceRequestRecord.getLocation());
        assertEquals(Long.valueOf(8), serviceRequestRecord.getChildLocation());
        assertEquals(Long.valueOf(9), serviceRequestRecord.getWorkflowStateId());
        assertEquals("ap.public", serviceRequestRecord.getTenantId());
        final List<AttributeEntry> actualAttributeEntries = serviceRequestRecord.getAttributeEntries();
        assertEquals(9, actualAttributeEntries.size());
    }

    private Date toDate(LocalDateTime localDateTime) {
        return new org.egov.pgr.common.date.Date(localDateTime).toDate();
    }

    @Test
    public void test_should_set_complainant_user_id_to_null_for_anonymous_complaint() {
        final HashMap<String, Object> sevaRequestMap = SevaRequestMapFactory.create();
        final HashMap<String, Object> requestInfo = (HashMap<String, Object>) sevaRequestMap.get("RequestInfo");
        final HashMap<String, Object> userInfo = (HashMap<String, Object>) requestInfo.get("userInfo");
        userInfo.put("type", "SYSTEM");
        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        final ServiceRequestRecord serviceRequestRecord = sevaRequest.toDomain();

        assertNotNull(serviceRequestRecord);
        assertNull(serviceRequestRecord.getComplainantUserId());
    }

    @Test
    public void test_should_set_complainant_user_id_to_null_for_when_employee_creates_complaint_on_behalf_of_citizen() {
        final HashMap<String, Object> sevaRequestMap = SevaRequestMapFactory.create();
        final HashMap<String, Object> requestInfo = (HashMap<String, Object>) sevaRequestMap.get("RequestInfo");
        final HashMap<String, Object> userInfo = (HashMap<String, Object>) requestInfo.get("userInfo");
        userInfo.put("type", "EMPLOYEE");
        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        final ServiceRequestRecord serviceRequestRecord = sevaRequest.toDomain();

        assertNotNull(serviceRequestRecord);
        assertNull(serviceRequestRecord.getComplainantUserId());
    }

    @Test
    public void test_should_set_complainant_user_id_when_logged_in_citizen_creates_complaint() {
        final HashMap<String, Object> sevaRequestMap = SevaRequestMapFactory.create();
        final HashMap<String, Object> requestInfo = (HashMap<String, Object>) sevaRequestMap.get("RequestInfo");
        final HashMap<String, Object> userInfo = (HashMap<String, Object>) requestInfo.get("userInfo");
        userInfo.put("type", "CITIZEN");
        userInfo.put("id", "4");
        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        final ServiceRequestRecord serviceRequestRecord = sevaRequest.toDomain();

        assertNotNull(serviceRequestRecord);
        assertEquals(Long.valueOf(4), serviceRequestRecord.getComplainantUserId());
    }

}

