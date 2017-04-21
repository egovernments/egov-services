package org.egov.pgr.write.contracts.grievance;

import org.egov.pgr.write.model.ComplaintRecord;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SevaRequestTest {

    /*

    {
   "RequestInfo":{
      "api_id":"org.egov.pgr",
      "ver":"1.0",
      "ts":"04-01-2017 01:20:47",
      "action":"POST",
      "did":"4354648646",
      "key":"xyz",
      "msg_id":"654654",
      "requester_id":"2",
      "auth_token":null,
      "correlationId":"33c76d89-8ec3-44bd-bb80-39e2f11cb199"
   },
   "ServiceRequest":{
      "tenantId":"ap.public",
      "service_request_id":"00626-2017-HO",
      "status":true,
      "service_name":"Obstruction of water flow",
      "service_code":"OOWF",
      "description":"foobaseasdasda",
      "agency_responsible":null,
      "service_notice":null,
      "requested_datetime":"01-04-2017 13:20:47",
      "updated_datetime":null,
      "expected_datetime":1491205848337,
      "address":"",
      "address_id":"256",
      "zipcode":null,
      "lat":0.0,
      "lng":0.0,
      "media_urls":null,
      "first_name":"rashmi",
      "last_name":null,
      "phone":"1234567890",
      "email":"rashmi@mi.com",
      "device_id":null,
      "account_id":null,
      "values":{
         "receivingMode":"Website",
         "receivingCenter":"",
         "status":"REGISTERED",
         "complainantAddress":"",
         "locationId":"173",
         "location_name":"Election Ward No 1",
         "child_location_id":"39",
         "assignment_id":"2",
         "stateId":"673",
         "designationId":"29"
      }
   }
}

     */

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
        assertEquals("firstName", complaintRecord.getComplainantName());
        assertEquals("1234567890", complaintRecord.getComplainantMobileNumber());
        assertEquals("email@email.com", complaintRecord.getComplainantEmail());
        assertEquals("complainant address", complaintRecord.getComplainantAddress());
        assertEquals("receiving mode", complaintRecord.getReceivingMode());
        assertEquals(Long.valueOf(5), complaintRecord.getReceivingCenter());
        assertEquals("complaintTypeCode", complaintRecord.getComplaintTypeCode());
        assertEquals(Long.valueOf(6), complaintRecord.getAssigneeId());
        assertEquals("REGISTERED", complaintRecord.getComplaintStatus());
        assertEquals(Long.valueOf(7), complaintRecord.getLocation());
        assertEquals(Long.valueOf(8), complaintRecord.getChildLocation());
        assertEquals(Long.valueOf(9), complaintRecord.getWorkflowStateId());
        assertEquals("ap.public", complaintRecord.getTenantId());
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

    @Test
    @SuppressWarnings("unchecked")
    public void test_should_return_receiving_center_as_null_when_not_present_in_request_map() {
        final HashMap<String, Object> sevaRequestMap = SevaRequestMapFactory.create();
        final HashMap<String, Object> serviceRequest = (HashMap<String, Object>) sevaRequestMap.get("ServiceRequest");
        final HashMap<String, String> values = (HashMap<String, String>) serviceRequest.get("values");
        values.put("receivingCenter", "");

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        final ComplaintRecord complaintRecord = sevaRequest.toDomain();

        assertNull(complaintRecord.getReceivingCenter());
    }




}

