package org.egov.domain.model;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

import static org.junit.Assert.*;

public class SevaRequestTest {

    private static final String IST = "Asia/Calcutta";

    @Test
    public void test_should_retrieve_mobile_number_from_request_map() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        sevaRequestMap.put("phone", "mobileNumber");

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertEquals("mobileNumber", sevaRequest.getMobileNumber());
    }

    @Test
    public void test_should_retrieve_email_from_request_map() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        sevaRequestMap.put("email", "email@email.com");

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertEquals("email@email.com", sevaRequest.getComplainantEmail());
    }

    @Test
    public void test_should_return_false_when_email_is_present_in_request_map() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        sevaRequestMap.put("email", "email@email.com");

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertFalse(sevaRequest.isComplainantEmailAbsent());
    }

    @Test
    public void test_should_return_true_when_email_is_not_present_in_request_map() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertTrue(sevaRequest.isComplainantEmailAbsent());
    }

    @Test
    public void test_should_return_complaint_type_name_from_request_map() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        sevaRequestMap.put("service_name", "complaintTypeName");

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertEquals("complaintTypeName", sevaRequest.getComplaintTypeName());
    }

    @Test
    public void test_should_return_complaint_crn_from_request_map() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        sevaRequestMap.put("service_request_id", "CRN");

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertEquals("CRN", sevaRequest.getCrn());
    }

    @Test
    public void test_should_return_complaint_status_name_from_request_map() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final HashMap<String, String> values = new HashMap<>();
        values.put("status", "statusName");
        sevaRequestMap.put("values", values);

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertEquals("statusName", sevaRequest.getStatusName());
    }

    @Test
    public void test_should_return_created_date_formatted_from_request_map() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final Date createdDate = Date.from(LocalDateTime.of(2016, 1, 2, 3, 4)
            .atZone(ZoneId.of(IST)).toInstant());
        sevaRequestMap.put("requested_datetime", createdDate);

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertEquals("02/01/2016 03:04", sevaRequest.getFormattedCreatedDate());
    }

    @Test
    public void test_should_return_complaint_description_from_request_map() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        sevaRequestMap.put("description", "complaintDescription");

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertEquals("complaintDescription", sevaRequest.getDetails());
    }

    @Test
    public void test_should_return_complainant_name_from_request_map() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        sevaRequestMap.put("description", "complaintDescription");

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertEquals("complaintDescription", sevaRequest.getDetails());
    }

    @Test
    public void test_should_return_location_name_from_request_map() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        final HashMap<String, String> values = new HashMap<>();
        values.put("location_name", "locationName");
        sevaRequestMap.put("values", values);

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertEquals("locationName", sevaRequest.getLocationName());
    }

    @Test
    public void test_should_return_first_name_from_request_map() {
        final HashMap<String, Object> sevaRequestMap = new HashMap<>();
        sevaRequestMap.put("first_name", "firstName");

        final SevaRequest sevaRequest = new SevaRequest(sevaRequestMap);

        assertEquals("firstName", sevaRequest.getComplainantName());
    }

}