package org.egov.pgrrest.write.contracts.grievance;

import org.egov.pgrrest.common.model.AttributeEntry;
import org.egov.pgrrest.write.model.ComplaintRecord;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class SevaRequestTest {

    @Test
    public void test_should_convert_from_seva_request_map_to_domain_complaint_record() {
        final HashMap<String, Object> sevaRequestMap = SevaRequestMapFactory.create();
        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        final ComplaintRecord complaintRecord = sevaRequest.toDomain();

        assertNotNull(complaintRecord);
        assertEquals("crn", complaintRecord.getCRN());
        assertEquals(new Date(1491205848337L), complaintRecord.getEscalationDate());
        assertEquals(Long.valueOf(29), complaintRecord.getDepartment());
        assertEquals(Long.valueOf(3), complaintRecord.getCreatedBy());
        assertEquals(Long.valueOf(3), complaintRecord.getLastModifiedBy());
        assertEquals(0.0d, complaintRecord.getLatitude(), 0);
        assertEquals(0.0d, complaintRecord.getLongitude(), 0);
        assertEquals("complaint description", complaintRecord.getDescription());
        assertEquals("landmark", complaintRecord.getLandmarkDetails());
        assertEquals("firstName", complaintRecord.getRequesterName());
        assertEquals("1234567890", complaintRecord.getRequesterMobileNumber());
        assertEquals("email@email.com", complaintRecord.getRequesterEmail());
        assertEquals("complainant address", complaintRecord.getRequesterAddress());
        assertEquals("receiving mode", complaintRecord.getReceivingMode());
        assertEquals(Long.valueOf(5), complaintRecord.getReceivingCenter());
        assertEquals("complaintTypeCode", complaintRecord.getServiceRequestTypeCode());
        assertEquals(Long.valueOf(6), complaintRecord.getAssigneeId());
        assertEquals("REGISTERED", complaintRecord.getServiceRequestStatus());
        assertEquals(Long.valueOf(7), complaintRecord.getLocation());
        assertEquals(Long.valueOf(8), complaintRecord.getChildLocation());
        assertEquals(Long.valueOf(9), complaintRecord.getWorkflowStateId());
        assertEquals("ap.public", complaintRecord.getTenantId());
        final List<AttributeEntry> actualAttributeEntries = complaintRecord.getAttributeEntries();
        assertEquals(9, actualAttributeEntries.size());
    }

    @Test
    public void test_should_set_complainant_user_id_to_null_for_anonymous_complaint() {
        final HashMap<String, Object> sevaRequestMap = SevaRequestMapFactory.create();
        final HashMap<String, Object> requestInfo = (HashMap<String, Object>) sevaRequestMap.get("RequestInfo");
        final HashMap<String, Object> userInfo = (HashMap<String, Object>) requestInfo.get("userInfo");
        userInfo.put("type", "SYSTEM");
        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        final ComplaintRecord complaintRecord = sevaRequest.toDomain();

        assertNotNull(complaintRecord);
        assertNull(complaintRecord.getComplainantUserId());
    }

    @Test
    public void test_should_set_complainant_user_id_to_null_for_when_employee_creates_complaint_on_behalf_of_citizen() {
        final HashMap<String, Object> sevaRequestMap = SevaRequestMapFactory.create();
        final HashMap<String, Object> requestInfo = (HashMap<String, Object>) sevaRequestMap.get("RequestInfo");
        final HashMap<String, Object> userInfo = (HashMap<String, Object>) requestInfo.get("userInfo");
        userInfo.put("type", "EMPLOYEE");
        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        final ComplaintRecord complaintRecord = sevaRequest.toDomain();

        assertNotNull(complaintRecord);
        assertNull(complaintRecord.getComplainantUserId());
    }

    @Test
    public void test_should_set_complainant_user_id_when_logged_in_citizen_creates_complaint() {
        final HashMap<String, Object> sevaRequestMap = SevaRequestMapFactory.create();
        final HashMap<String, Object> requestInfo = (HashMap<String, Object>) sevaRequestMap.get("RequestInfo");
        final HashMap<String, Object> userInfo = (HashMap<String, Object>) requestInfo.get("userInfo");
        userInfo.put("type", "CITIZEN");
        userInfo.put("id", "4");
        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        final ComplaintRecord complaintRecord = sevaRequest.toDomain();

        assertNotNull(complaintRecord);
        assertEquals(Long.valueOf(4), complaintRecord.getComplainantUserId());
    }

}

