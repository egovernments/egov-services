package org.egov.domain.model;

import org.junit.Test;

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
        sevaRequestMap.put("ServiceRequest", serviceRequest);

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertEquals("mobileNumber", sevaRequest.getMobileNumber());
    }

    @Test
    public void test_should_retrieve_email_from_request_map() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final HashMap<String, Object> serviceRequest = new HashMap<>();
        serviceRequest.put("email", "email@email.com");
        sevaRequestMap.put("ServiceRequest", serviceRequest);

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertEquals("email@email.com", sevaRequest.getComplainantEmail());
    }

    @Test
    public void test_should_return_false_when_email_is_present_in_request_map() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final HashMap<String, Object> serviceRequest = new HashMap<>();
        serviceRequest.put("email", "email@email.com");
        sevaRequestMap.put("ServiceRequest", serviceRequest);

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertFalse(sevaRequest.isComplainantEmailAbsent());
    }

    @Test
    public void test_should_return_true_when_email_is_not_present_in_request_map() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        sevaRequestMap.put("ServiceRequest", new HashMap<String, Object>());

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertTrue(sevaRequest.isComplainantEmailAbsent());
    }

    @Test
    public void test_should_return_complaint_type_name_from_request_map() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final HashMap<String, Object> serviceRequest = new HashMap<>();
        serviceRequest.put("service_name", "complaintTypeName");
        sevaRequestMap.put("ServiceRequest", serviceRequest);

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertEquals("complaintTypeName", sevaRequest.getComplaintTypeName());
    }

    @Test
    public void test_should_return_complaint_crn_from_request_map() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final HashMap<String, Object> serviceRequest = new HashMap<>();
        serviceRequest.put("service_request_id", "CRN");
        sevaRequestMap.put("ServiceRequest", serviceRequest);

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertEquals("CRN", sevaRequest.getCrn());
    }

    @Test
    public void test_should_return_complaint_status_name_from_request_map() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final HashMap<String, Object> serviceRequest = new HashMap<>();
        final HashMap<String, String> values = new HashMap<>();
        values.put("status", "statusName");
        serviceRequest.put("values", values);
        sevaRequestMap.put("ServiceRequest", serviceRequest);

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertEquals("statusName", sevaRequest.getStatusName());
    }

    @Test
    public void test_should_return_created_date_formatted_from_request_map() {
        final HashMap<String, Object> serviceRequest = new HashMap<>();
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        TimeZone.setDefault(TimeZone.getTimeZone(IST));
        serviceRequest.put("requested_datetime", "26-03-2017 12:58:40");
        sevaRequestMap.put("ServiceRequest", serviceRequest);

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertEquals("26/03/2017 12:58", sevaRequest.getFormattedCreatedDate());
    }

    @Test(expected = RuntimeException.class)
    public void test_should_throw_run_time_exception_when_date_format_is_not_as_expected() {
        final HashMap<String, Object> serviceRequest = new HashMap<>();
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        TimeZone.setDefault(TimeZone.getTimeZone(IST));
        serviceRequest.put("requested_datetime", "26/03/2017 12:58:40");
        sevaRequestMap.put("ServiceRequest", serviceRequest);
        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        sevaRequest.getFormattedCreatedDate();
    }


    @Test
    public void test_should_return_complaint_description_from_request_map() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final HashMap<String, Object> serviceRequest = new HashMap<>();
        serviceRequest.put("description", "complaintDescription");
        sevaRequestMap.put("ServiceRequest", serviceRequest);

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertEquals("complaintDescription", sevaRequest.getDetails());
    }

    @Test
    public void test_should_return_complainant_name_from_request_map() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final HashMap<String, Object> serviceRequest = new HashMap<>();
        serviceRequest.put("first_name", "firstName");
        sevaRequestMap.put("ServiceRequest", serviceRequest);

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertEquals("firstName", sevaRequest.getComplainantName());
    }

    //TODO: Remove once UserInfo is used to set firstName in PGR-REST
    @Test
    public void test_should_return_place_holder_value_when_first_name_not_present_in_request_map() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        sevaRequestMap.put("ServiceRequest", new HashMap<String, Object>());

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertEquals("placeholder", sevaRequest.getComplainantName());
    }

    @Test
    public void test_should_return_location_name_from_request_map() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final HashMap<String, Object> serviceRequest = new HashMap<>();
        final HashMap<String, String> values = new HashMap<>();
        values.put("location_name", "locationName");
        serviceRequest.put("values", values);
        sevaRequestMap.put("ServiceRequest", serviceRequest);

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertEquals("locationName", sevaRequest.getLocationName());
    }

    @Test
    public void test_should_return_first_name_from_request_map() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final HashMap<String, Object> serviceRequest = new HashMap<>();
        serviceRequest.put("first_name", "firstName");
        sevaRequestMap.put("ServiceRequest", serviceRequest);

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertEquals("firstName", sevaRequest.getComplainantName());
    }

}