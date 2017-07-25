package org.egov.pgrrest.common.contract;

import org.egov.pgr.common.contract.AttributeEntry;
import org.egov.pgrrest.common.domain.model.AuthenticatedUser;
import org.egov.pgrrest.common.domain.model.UserType;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class ServiceRequestTest {

    @Test
    public void test_should_return_location_id_from_attribute_values_field_when_flag_is_enabled() {
        org.egov.pgrrest.common.contract.web.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.web
            .ServiceRequest.builder()
            .attribValues(Collections.singletonList(new AttributeEntry("systemLocationId", "location")))
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals("location", complaint.getServiceRequestLocation().getLocationId());
    }

    @Test
    public void test_should_return_receiving_center_from_attribute_values_field_when_flag_is_enabled() {
        final AttributeEntry attributeEntry = new AttributeEntry("systemReceivingCenter", "receivingCenter");
        org.egov.pgrrest.common.contract.web.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.web
            .ServiceRequest.builder()
            .attribValues(Collections.singletonList(attributeEntry))
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals("receivingCenter", complaint.getReceivingCenter());
    }


    @Test
    public void test_should_return_receiving_mode_from_attribute_values_field_when_flag_is_enabled() {
        final AttributeEntry attributeEntry = new AttributeEntry("systemReceivingMode", "receivingMode");
        org.egov.pgrrest.common.contract.web.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.web
            .ServiceRequest.builder()
            .attribValues(Collections.singletonList(attributeEntry))
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals("receivingMode", complaint.getReceivingMode());
    }

    @Test
    public void test_should_copy_over_location_coordinates_to_domain_complaint() {
        Double lat = 12.343, lng = 23.243;
        org.egov.pgrrest.common.contract.web.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.web
            .ServiceRequest.builder()
            .latitude(lat)
            .longitude(lng)
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals(lat, complaint.getServiceRequestLocation().getCoordinates().getLatitude());
        assertEquals(lng, complaint.getServiceRequestLocation().getCoordinates().getLongitude());
    }

    @Test
    public void test_should_copy_cross_hierarchy_id_to_domain_complaint() {
        String crossHierarchyId = "12";
        org.egov.pgrrest.common.contract.web.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.web
            .ServiceRequest.builder()
            .crossHierarchyId(crossHierarchyId)
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals(crossHierarchyId, complaint.getServiceRequestLocation().getCrossHierarchyId());
    }

    @Test
    public void test_should_copy_complaint_type_to_domain_complaint() {
        final String complaintTypeCode = "complaintTypeCode";
        final String complaintTypeName = "complaintTypeName";
        org.egov.pgrrest.common.contract.web.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.web
            .ServiceRequest.builder()
            .serviceTypeCode(complaintTypeCode)
            .serviceTypeName(complaintTypeName)
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertNotNull(complaint.getServiceRequestType());
        assertEquals(complaintTypeCode, complaint.getServiceRequestType().getCode());
        assertEquals(complaintTypeName, complaint.getServiceRequestType().getName());
    }

    @Test
    public void test_should_copy_tenant_id_to_domain_complaint() {
        final String tenantId = "tenantId";
        org.egov.pgrrest.common.contract.web.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.web
            .ServiceRequest.builder()
            .tenantId(tenantId)
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertNotNull(complaint.getServiceRequestType());
        assertEquals(tenantId, complaint.getTenantId());
    }

    @Test
    public void test_should_copy_address_to_domain_complaint() {
        final String address = "address";
        org.egov.pgrrest.common.contract.web.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.web
            .ServiceRequest.builder()
            .address(address)
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals(address, complaint.getAddress());
    }

    @Test
    public void test_should_copy_description_to_domain_complaint() {
        final String description = "description";
        org.egov.pgrrest.common.contract.web.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.web
            .ServiceRequest.builder()
            .address(description)
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals(description, complaint.getAddress());
    }

    @Test
    public void test_should_copy_media_urls_to_domain_complaint() {
        final List<String> mediaUrls = Arrays.asList("url1", "url2");
        org.egov.pgrrest.common.contract.web.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.web
            .ServiceRequest.builder()
            .mediaUrls(mediaUrls)
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertNotNull(complaint.getMediaUrls());
        assertArrayEquals(mediaUrls.toArray(), complaint.getMediaUrls().toArray());
    }


    @Test
    public void test_should_copy_user_id_to_domain_complaint_from_attribute_values_field_when_flag_is_enabled() {
        final HashMap<String, String> values = new HashMap<>();
        org.egov.pgrrest.common.contract.web.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.web
            .ServiceRequest.builder()
            .attribValues(Collections.singletonList(new AttributeEntry("systemUserId", "userId")))
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals("userId", complaint.getRequester().getUserId());
    }

    @Test
    public void test_should_copy_complainant_first_name_to_domain_complaint() {
        final String firstName = "first Name";
        org.egov.pgrrest.common.contract.web.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.web
            .ServiceRequest.builder()
            .firstName(firstName)
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals(firstName, complaint.getRequester().getFirstName());
    }

    @Test
    public void test_should_copy_complainant_email_to_domain_complaint() {
        final String email = "email";
        org.egov.pgrrest.common.contract.web.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.web
            .ServiceRequest.builder()
            .email(email)
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals(email, complaint.getRequester().getEmail());
    }

    @Test
    public void test_should_copy_complainant_mobile_number_to_domain_complaint() {
        final String mobileNumber = "mobileNumber";
        org.egov.pgrrest.common.contract.web.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.web
            .ServiceRequest.builder()
            .phone(mobileNumber)
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals(mobileNumber, complaint.getRequester().getMobile());
    }

    @Test
    public void test_should_copy_complainant_address_from_attribute_values_field_when_flag_is_enabled() {
        org.egov.pgrrest.common.contract.web.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.web
            .ServiceRequest.builder()
            .attribValues(Collections.singletonList(new AttributeEntry("systemRequesterAddress", "address")))
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals("address", complaint.getRequester().getAddress());
    }

    @Test
    public void test_should_copy_crn_to_domain_complaint() {
        String crn = "crn";
        org.egov.pgrrest.common.contract.web.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.web
            .ServiceRequest.builder()
            .crn(crn)
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals(crn, complaint.getCrn());
    }

    @Test
    public void test_should_set_modify_flag_to_false_when_creating_domain_complaint_from_create_seva_request() {
        String crn = "crn";
        org.egov.pgrrest.common.contract.web.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.web
            .ServiceRequest.builder()
            .crn(crn)
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertFalse(complaint.isModifyServiceRequest());
    }

    @Test
    public void test_should_set_modify_flag_to_true_when_creating_domain_complaint_from_update_seva_request() {
        String crn = "crn";
        org.egov.pgrrest.common.contract.web.ServiceRequest serviceRequest =
            org.egov.pgrrest.common.contract.web.ServiceRequest.builder()
                .crn(crn)
                .build();

        ServiceRequest complaint = serviceRequest.toDomainForUpdateRequest(getAuthenticatedUser());

        assertTrue(complaint.isModifyServiceRequest());
    }

    private AuthenticatedUser getAuthenticatedUser() {
        return AuthenticatedUser.builder()
            .id(1L)
            .type(UserType.CITIZEN)
            .build();
    }

}