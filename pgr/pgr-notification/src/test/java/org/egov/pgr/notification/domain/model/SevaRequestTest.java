package org.egov.pgr.notification.domain.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class SevaRequestTest {

    private static final String IST = "Asia/Calcutta";

    @Test
    public void test_should_retrieve_mobile_number_from_request_map() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final HashMap<String, Object> serviceRequest = new HashMap<>();
        serviceRequest.put("phone", "mobileNumber");
        sevaRequestMap.put("serviceRequest", serviceRequest);

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertEquals("mobileNumber", sevaRequest.getMobileNumber());
    }

    @Test
    public void test_should_retrieve_email_from_request_map() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final HashMap<String, Object> serviceRequest = new HashMap<>();
        serviceRequest.put("email", "email@email.com");
        sevaRequestMap.put("serviceRequest", serviceRequest);

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertEquals("email@email.com", sevaRequest.getRequesterEmail());
    }

    @Test
    public void test_should_return_false_when_email_is_present_in_request_map() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final HashMap<String, Object> serviceRequest = new HashMap<>();
        serviceRequest.put("email", "email@email.com");
        sevaRequestMap.put("serviceRequest", serviceRequest);

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertFalse(sevaRequest.isRequesterEmailAbsent());
    }

    @Test
    public void test_should_return_true_when_email_is_not_present_in_request_map() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        sevaRequestMap.put("serviceRequest", new HashMap<String, Object>());

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertTrue(sevaRequest.isRequesterEmailAbsent());
    }

    @Test
    public void test_should_return_complaint_type_name_from_request_map() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final HashMap<String, Object> serviceRequest = new HashMap<>();
        serviceRequest.put("serviceName", "complaintTypeName");
        sevaRequestMap.put("serviceRequest", serviceRequest);

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertEquals("complaintTypeName", sevaRequest.getServiceTypeName());
    }

    @Test
    public void test_should_return_complaint_crn_from_request_map() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final HashMap<String, Object> serviceRequest = new HashMap<>();
        serviceRequest.put("serviceRequestId", "CRN");
        sevaRequestMap.put("serviceRequest", serviceRequest);

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertEquals("CRN", sevaRequest.getCrn());
    }

    @Test
    public void test_should_return_complaint_status_name_from_request_map() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final HashMap<String, Object> serviceRequest = new HashMap<>();
        final ArrayList<HashMap<String, String>> attributeValues = new ArrayList<>();
        final HashMap<String, String> locationEntry = new HashMap<>();
        locationEntry.put("key", "systemStatus");
        locationEntry.put("name", "systemStatusName");
        attributeValues.add(locationEntry);
        serviceRequest.put("attribValues", attributeValues);
        serviceRequest.put("attribValues", attributeValues);
        sevaRequestMap.put("serviceRequest", serviceRequest);

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertEquals("systemStatusName", sevaRequest.getStatusName());
    }

    @Test
    public void test_should_return_created_date_formatted_from_request_map() {
        final HashMap<String, Object> serviceRequest = new HashMap<>();
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        TimeZone.setDefault(TimeZone.getTimeZone(IST));
        serviceRequest.put("requestedDatetime", "26-03-2017 12:58:40");
        sevaRequestMap.put("serviceRequest", serviceRequest);

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertEquals("26/03/2017 12:58", sevaRequest.getFormattedCreatedDate());
    }

    @Test(expected = RuntimeException.class)
    public void test_should_throw_run_time_exception_when_date_format_is_not_as_expected() {
        final HashMap<String, Object> serviceRequest = new HashMap<>();
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        TimeZone.setDefault(TimeZone.getTimeZone(IST));
        serviceRequest.put("requestedDatetime", "26/03/2017 12:58:40");
        sevaRequestMap.put("serviceRequest", serviceRequest);
        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        sevaRequest.getFormattedCreatedDate();
    }


    @Test
    public void test_should_return_complaint_description_from_request_map() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final HashMap<String, Object> serviceRequest = new HashMap<>();
        serviceRequest.put("description", "complaintDescription");
        sevaRequestMap.put("serviceRequest", serviceRequest);

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertEquals("complaintDescription", sevaRequest.getDetails());
    }

    @Test
    public void test_should_return_complainant_name_from_request_map() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final HashMap<String, Object> serviceRequest = new HashMap<>();
        serviceRequest.put("firstName", "firstName");
        sevaRequestMap.put("serviceRequest", serviceRequest);

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertEquals("firstName", sevaRequest.getRequesterName());
    }

    @Test
    public void test_should_return_tenant_id_from_request_map() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final HashMap<String, Object> serviceRequest = new HashMap<>();
        serviceRequest.put("tenantId", "tenant");
        sevaRequestMap.put("serviceRequest", serviceRequest);

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertEquals("tenant", sevaRequest.getTenantId());
    }

    @Test
    public void test_should_return_location_name_from_request_map() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final HashMap<String, Object> serviceRequest = new HashMap<>();
        final ArrayList<HashMap<String, String>> attributeValues = new ArrayList<>();
        final HashMap<String, String> locationEntry = new HashMap<>();
        locationEntry.put("key", "systemLocationName");
        locationEntry.put("name", "systemLocationName");
        attributeValues.add(locationEntry);
        serviceRequest.put("attribValues", attributeValues);
        sevaRequestMap.put("serviceRequest", serviceRequest);

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertEquals("systemLocationName", sevaRequest.getLocationName());
    }

    @Test
    public void test_should_return_first_name_from_request_map() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final HashMap<String, Object> serviceRequest = new HashMap<>();
        serviceRequest.put("firstName", "firstName");
        sevaRequestMap.put("serviceRequest", serviceRequest);

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertEquals("firstName", sevaRequest.getRequesterName());
    }
    
    @Test
    public void test_should_return_list_of_rejection_letter_from_request_map() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final HashMap<String, Object> serviceRequest = new HashMap<>();
        final ArrayList<HashMap<String, String>> attributeValues = new ArrayList<>();
        final HashMap<String, String> rejection = new HashMap<>();
        rejection.put("key", "rejectionLetter");
        rejection.put("name", "rejectionLetter");
        attributeValues.add(rejection);
        serviceRequest.put("attribValues", attributeValues);
        sevaRequestMap.put("serviceRequest", serviceRequest);

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertEquals("rejectionLetter",sevaRequest.getLatestRejectionLetter());
    }
    
    @Test
    public void test_should_fail_while_passing_empty_list_of_rejection_letter() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final HashMap<String, Object> serviceRequest = new HashMap<>();
        final ArrayList<HashMap<String, String>> attributeValues = new ArrayList<>();
        final HashMap<String, String> rejection = new HashMap<>();
        rejection.put("key", null);
        rejection.put("name", "rejectionLetter");
        attributeValues.add(rejection);
        serviceRequest.put("attribValues", attributeValues);
        sevaRequestMap.put("serviceRequest", serviceRequest);

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertNull(sevaRequest.getLatestRejectionLetter());
    }

}