package org.egov.pgr.persistence.queue.contract;

import org.egov.pgr.domain.model.AuthenticatedUser;
import org.egov.pgr.domain.model.Complaint;
import org.egov.pgr.domain.model.UserType;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class ServiceRequestTest {

    @Test
    public void test_should_copy_over_location_id_to_domain_complaint() {
        String expectedLocationId = "12";
        Map<String, String> values = new HashMap<>();
        values.put("locationId", expectedLocationId);
        ServiceRequest serviceRequest = ServiceRequest.builder()
                .values(values)
                .build();

        Complaint complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals(expectedLocationId, complaint.getComplaintLocation().getLocationId());
    }

    @Test
    public void test_should_set_location_id_to_null_when_not_provided() {
        ServiceRequest serviceRequest = ServiceRequest.builder()
                .values(new HashMap<>())
                .build();

        Complaint complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertNull(complaint.getComplaintLocation().getCrossHierarchyId());
        assertNull(complaint.getComplaintLocation().getCoordinates().getLatitude());
        assertNull(complaint.getComplaintLocation().getCoordinates().getLongitude());

        assertNull(complaint.getComplaintLocation().getLocationId());
    }

    @Test
    public void test_should_copy_over_location_coordinates_to_domain_complaint() {
        Double lat = 12.343, lng = 23.243;
        ServiceRequest serviceRequest = ServiceRequest.builder()
                .latitude(lat)
                .longitude(lng)
                .values(new HashMap<>())
                .build();

        Complaint complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals(lat, complaint.getComplaintLocation().getCoordinates().getLatitude());
        assertEquals(lng, complaint.getComplaintLocation().getCoordinates().getLongitude());
    }

    @Test
    public void test_should_copy_cross_hierarchy_id_to_domain_complaint() {
        String crossHierarchyId = "12";
        ServiceRequest serviceRequest = ServiceRequest.builder()
                .crossHierarchyId(crossHierarchyId)
                .values(new HashMap<>())
                .build();

        Complaint complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals(crossHierarchyId, complaint.getComplaintLocation().getCrossHierarchyId());
    }

    @Test
    public void test_should_copy_complaint_type_to_domain_complaint() {
        final String complaintTypeCode = "complaintTypeCode";
        final String complaintTypeName = "complaintTypeName";
        ServiceRequest serviceRequest = ServiceRequest.builder()
                .values(new HashMap<>())
                .complaintTypeCode(complaintTypeCode)
                .complaintTypeName(complaintTypeName)
                .build();

        Complaint complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertNotNull(complaint.getComplaintType());
        assertEquals(complaintTypeCode, complaint.getComplaintType().getCode());
        assertEquals(complaintTypeName, complaint.getComplaintType().getName());
    }

    @Test
    public void test_should_copy_tenant_id_to_domain_complaint() {
        final String tenantId = "tenantId";
        ServiceRequest serviceRequest = ServiceRequest.builder()
                .values(new HashMap<>())
                .tenantId(tenantId)
                .build();

        Complaint complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertNotNull(complaint.getComplaintType());
        assertEquals(tenantId, complaint.getTenantId());
    }

    @Test
    public void test_should_copy_address_to_domain_complaint() {
        final String address = "address";
        ServiceRequest serviceRequest = ServiceRequest.builder()
                .values(new HashMap<>())
                .address(address)
                .build();

        Complaint complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals(address, complaint.getAddress());
    }

    @Test
    public void test_should_copy_description_to_domain_complaint() {
        final String description = "description";
        ServiceRequest serviceRequest = ServiceRequest.builder()
                .values(new HashMap<>())
                .address(description)
                .build();

        Complaint complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals(description, complaint.getAddress());
    }

    @Test
    public void test_should_copy_media_urls_to_domain_complaint() {
        final List<String> mediaUrls = Arrays.asList("url1", "url2");
        ServiceRequest serviceRequest = ServiceRequest.builder()
                .values(new HashMap<>())
                .mediaUrls(mediaUrls)
                .build();

        Complaint complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertNotNull(complaint.getMediaUrls());
        assertArrayEquals(mediaUrls.toArray(), complaint.getMediaUrls().toArray());
    }

    @Test
    public void test_should_copy_receiving_source_to_domain_complaint() {
        final String receivingMode = "receivingMode";
        final String receivingCenter = "receivingCenter";
        final HashMap<String, String> values = new HashMap<>();
        values.put("receivingMode", receivingMode);
        values.put("receivingCenter", receivingCenter);
        ServiceRequest serviceRequest = ServiceRequest.builder()
                .values(values)
                .build();

        Complaint complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals(receivingMode, complaint.getReceivingMode());
        assertEquals(receivingCenter, complaint.getReceivingCenter());
    }

    @Test
    public void test_should_copy_user_id_to_domain_complaint() {
        final HashMap<String, String> values = new HashMap<>();
        final String complainantUserId = "userId";
        values.put("userId", complainantUserId);
        ServiceRequest serviceRequest = ServiceRequest.builder()
                .values(values)
                .build();

        Complaint complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals(complainantUserId, complaint.getComplainant().getUserId());
    }

    @Test
    public void test_should_copy_complainant_first_name_to_domain_complaint() {
        final String firstName = "first Name";
        ServiceRequest serviceRequest = ServiceRequest.builder()
                .values(new HashMap<>())
                .firstName(firstName)
                .build();

        Complaint complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals(firstName, complaint.getComplainant().getFirstName());
    }

    @Test
    public void test_should_copy_complainant_email_to_domain_complaint() {
        final String email = "email";
        ServiceRequest serviceRequest = ServiceRequest.builder()
                .values(new HashMap<>())
                .email(email)
                .build();

        Complaint complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals(email, complaint.getComplainant().getEmail());
    }

    @Test
    public void test_should_copy_complainant_mobile_number_to_domain_complaint() {
        final String mobileNumber = "mobileNumber";
        ServiceRequest serviceRequest = ServiceRequest.builder()
                .values(new HashMap<>())
                .phone(mobileNumber)
                .build();

        Complaint complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals(mobileNumber, complaint.getComplainant().getMobile());
    }

    @Test
    public void test_should_copy_crn_to_domain_complaint() {
        String crn = "crn";
        ServiceRequest serviceRequest = ServiceRequest.builder()
                .values(new HashMap<>())
                .crn(crn)
                .build();

        Complaint complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals(crn, complaint.getCrn());
    }

    @Test
    public void test_should_set_modify_flag_to_false_when_creating_domain_complaint_from_create_seva_request() {
        String crn = "crn";
        ServiceRequest serviceRequest = ServiceRequest.builder()
                .values(new HashMap<>())
                .crn(crn)
                .build();

        Complaint complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertFalse(complaint.isModifyComplaint());
    }

    @Test
    public void test_should_set_modify_flag_to_true_when_creating_domain_complaint_from_update_seva_request() {
        String crn = "crn";
        ServiceRequest serviceRequest = ServiceRequest.builder()
                .values(new HashMap<>())
                .crn(crn)
                .build();

        Complaint complaint = serviceRequest.toDomainForUpdateRequest(getAuthenticatedUser());

        assertTrue(complaint.isModifyComplaint());
    }


    private AuthenticatedUser getAuthenticatedUser() {
        return AuthenticatedUser.builder()
                .id(1)
                .type(UserType.CITIZEN)
                .build();
    }

}