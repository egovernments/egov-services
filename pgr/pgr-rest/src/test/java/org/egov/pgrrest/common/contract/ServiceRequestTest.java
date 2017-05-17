package org.egov.pgrrest.common.contract;

import org.egov.pgr.common.contract.AttributeEntry;
import org.egov.pgrrest.common.model.AuthenticatedUser;
import org.egov.pgrrest.common.model.Complainant;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.egov.pgrrest.common.model.UserType;
import org.egov.pgrrest.read.domain.model.ComplaintLocation;
import org.egov.pgrrest.read.domain.model.ComplaintType;
import org.egov.pgrrest.read.domain.model.Coordinates;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class ServiceRequestTest {

    @Test
    public void test_should_copy_over_location_id_to_domain_complaint() {
        String expectedLocationId = "12";
        Map<String, String> values = new HashMap<>();
        values.put("locationId", expectedLocationId);
        org.egov.pgrrest.common.contract.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.ServiceRequest.builder()
            .values(values)
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals(expectedLocationId, complaint.getComplaintLocation().getLocationId());
    }

    @Test
    public void test_should_set_location_id_to_null_when_not_provided() {
        org.egov.pgrrest.common.contract.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.ServiceRequest.builder()
            .values(new HashMap<>())
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertNull(complaint.getComplaintLocation().getCrossHierarchyId());
        assertNull(complaint.getComplaintLocation().getCoordinates().getLatitude());
        assertNull(complaint.getComplaintLocation().getCoordinates().getLongitude());
        assertNull(complaint.getComplaintLocation().getLocationId());
    }

    @Test
    public void test_should_return_receiving_center_from_values_field_when_flag_is_disabled() {
        final HashMap<String, String> values = new HashMap<>();
        values.put("receivingCenter", "receivingCenter");
        org.egov.pgrrest.common.contract.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.ServiceRequest.builder()
            .values(values)
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals("receivingCenter", complaint.getReceivingCenter());
    }

    @Test
    public void test_should_return_location_id_from_values_field_when_flag_is_disabled() {
        final HashMap<String, String> values = new HashMap<>();
        values.put("locationId", "location");
        org.egov.pgrrest.common.contract.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.ServiceRequest.builder()
            .values(values)
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals("location", complaint.getComplaintLocation().getLocationId());
    }

    @Test
    public void test_should_return_location_id_from_attribute_values_field_when_flag_is_enabled() {
        org.egov.pgrrest.common.contract.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.ServiceRequest.builder()
            .attribValues(Collections.singletonList(new AttributeEntry("locationId", "location")))
            .attribValuesPopulated(true)
            .values(new HashMap<>())
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals("location", complaint.getComplaintLocation().getLocationId());
    }

    @Test
    public void test_should_return_receiving_center_from_attribute_values_field_when_flag_is_enabled() {
        final AttributeEntry attributeEntry = new AttributeEntry("receivingCenter", "receivingCenter");
        org.egov.pgrrest.common.contract.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.ServiceRequest.builder()
            .attribValues(Collections.singletonList(attributeEntry))
            .attribValuesPopulated(true)
            .values(new HashMap<>())
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals("receivingCenter", complaint.getReceivingCenter());
    }

    @Test
    public void test_should_return_receiving_mode_from_values_field_when_flag_is_disabled() {
        final HashMap<String, String> values = new HashMap<>();
        values.put("receivingMode", "receivingMode");
        org.egov.pgrrest.common.contract.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.ServiceRequest.builder()
            .values(values)
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals("receivingMode", complaint.getReceivingMode());
    }

    @Test
    public void test_should_return_receiving_mode_from_attribute_values_field_when_flag_is_enabled() {
        final AttributeEntry attributeEntry = new AttributeEntry("receivingMode", "receivingMode");
        org.egov.pgrrest.common.contract.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.ServiceRequest.builder()
            .attribValues(Collections.singletonList(attributeEntry))
            .attribValuesPopulated(true)
            .values(new HashMap<>())
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals("receivingMode", complaint.getReceivingMode());
    }

    @Test
    public void test_should_copy_over_location_coordinates_to_domain_complaint() {
        Double lat = 12.343, lng = 23.243;
        org.egov.pgrrest.common.contract.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.ServiceRequest.builder()
            .latitude(lat)
            .longitude(lng)
            .values(new HashMap<>())
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals(lat, complaint.getComplaintLocation().getCoordinates().getLatitude());
        assertEquals(lng, complaint.getComplaintLocation().getCoordinates().getLongitude());
    }

    @Test
    public void test_should_copy_cross_hierarchy_id_to_domain_complaint() {
        String crossHierarchyId = "12";
        org.egov.pgrrest.common.contract.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.ServiceRequest.builder()
            .crossHierarchyId(crossHierarchyId)
            .values(new HashMap<>())
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals(crossHierarchyId, complaint.getComplaintLocation().getCrossHierarchyId());
    }

    @Test
    public void test_should_copy_complaint_type_to_domain_complaint() {
        final String complaintTypeCode = "complaintTypeCode";
        final String complaintTypeName = "complaintTypeName";
        org.egov.pgrrest.common.contract.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.ServiceRequest.builder()
            .values(new HashMap<>())
            .complaintTypeCode(complaintTypeCode)
            .complaintTypeName(complaintTypeName)
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertNotNull(complaint.getComplaintType());
        assertEquals(complaintTypeCode, complaint.getComplaintType().getCode());
        assertEquals(complaintTypeName, complaint.getComplaintType().getName());
    }

    @Test
    public void test_should_copy_tenant_id_to_domain_complaint() {
        final String tenantId = "tenantId";
        org.egov.pgrrest.common.contract.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.ServiceRequest.builder()
            .values(new HashMap<>())
            .tenantId(tenantId)
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertNotNull(complaint.getComplaintType());
        assertEquals(tenantId, complaint.getTenantId());
    }

    @Test
    public void test_should_copy_address_to_domain_complaint() {
        final String address = "address";
        org.egov.pgrrest.common.contract.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.ServiceRequest.builder()
            .values(new HashMap<>())
            .address(address)
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals(address, complaint.getAddress());
    }

    @Test
    public void test_should_copy_description_to_domain_complaint() {
        final String description = "description";
        org.egov.pgrrest.common.contract.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.ServiceRequest.builder()
            .values(new HashMap<>())
            .address(description)
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals(description, complaint.getAddress());
    }

    @Test
    public void test_should_copy_media_urls_to_domain_complaint() {
        final List<String> mediaUrls = Arrays.asList("url1", "url2");
        org.egov.pgrrest.common.contract.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.ServiceRequest.builder()
            .values(new HashMap<>())
            .mediaUrls(mediaUrls)
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

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
        org.egov.pgrrest.common.contract.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.ServiceRequest.builder()
            .values(values)
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals(receivingMode, complaint.getReceivingMode());
        assertEquals(receivingCenter, complaint.getReceivingCenter());
    }

    @Test
    public void test_should_copy_user_id_to_domain_complaint_from_values_field_when_flag_is_disabled() {
        final HashMap<String, String> values = new HashMap<>();
        final String complainantUserId = "userId";
        values.put("userId", complainantUserId);
        org.egov.pgrrest.common.contract.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.ServiceRequest.builder()
            .values(values)
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals(complainantUserId, complaint.getRequester().getUserId());
    }

    @Test
    public void test_should_copy_user_id_to_domain_complaint_from_attribute_values_field_when_flag_is_enabled() {
        final HashMap<String, String> values = new HashMap<>();
        org.egov.pgrrest.common.contract.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.ServiceRequest.builder()
            .values(values)
            .attribValues(Collections.singletonList(new AttributeEntry("userId", "userId")))
            .attribValuesPopulated(true)
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals("userId", complaint.getRequester().getUserId());
    }

    @Test
    public void test_should_copy_complainant_first_name_to_domain_complaint() {
        final String firstName = "first Name";
        org.egov.pgrrest.common.contract.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.ServiceRequest.builder()
            .values(new HashMap<>())
            .firstName(firstName)
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals(firstName, complaint.getRequester().getFirstName());
    }

    @Test
    public void test_should_copy_complainant_email_to_domain_complaint() {
        final String email = "email";
        org.egov.pgrrest.common.contract.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.ServiceRequest.builder()
            .values(new HashMap<>())
            .email(email)
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals(email, complaint.getRequester().getEmail());
    }

    @Test
    public void test_should_copy_complainant_mobile_number_to_domain_complaint() {
        final String mobileNumber = "mobileNumber";
        org.egov.pgrrest.common.contract.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.ServiceRequest.builder()
            .values(new HashMap<>())
            .phone(mobileNumber)
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals(mobileNumber, complaint.getRequester().getMobile());
    }

    @Test
    public void test_should_copy_complainant_address_from_values_field_when_flag_is_disabled() {
        final HashMap<String, String> values = new HashMap<>();
        values.put("complainantAddress", "address");
        org.egov.pgrrest.common.contract.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.ServiceRequest.builder()
            .values(values)
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals("address", complaint.getRequester().getAddress());
    }

    @Test
    public void test_should_copy_complainant_address_from_attribute_values_field_when_flag_is_enabled() {
        org.egov.pgrrest.common.contract.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.ServiceRequest.builder()
            .attribValues(Collections.singletonList(new AttributeEntry("complainantAddress", "address")))
            .values(new HashMap<>())
            .attribValuesPopulated(true)
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals("address", complaint.getRequester().getAddress());
    }

    @Test
    public void test_should_copy_crn_to_domain_complaint() {
        String crn = "crn";
        org.egov.pgrrest.common.contract.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.ServiceRequest.builder()
            .values(new HashMap<>())
            .crn(crn)
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertEquals(crn, complaint.getCrn());
    }

    @Test
    public void test_should_set_modify_flag_to_false_when_creating_domain_complaint_from_create_seva_request() {
        String crn = "crn";
        org.egov.pgrrest.common.contract.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.ServiceRequest.builder()
            .values(new HashMap<>())
            .crn(crn)
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForCreateRequest(getAuthenticatedUser());

        assertFalse(complaint.isModifyServiceRequest());
    }

    @Test
    public void test_should_set_modify_flag_to_true_when_creating_domain_complaint_from_update_seva_request() {
        String crn = "crn";
        org.egov.pgrrest.common.contract.ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.ServiceRequest.builder()
            .values(new HashMap<>())
            .crn(crn)
            .build();

        ServiceRequest complaint = serviceRequest.toDomainForUpdateRequest(getAuthenticatedUser());

        assertTrue(complaint.isModifyServiceRequest());
    }

    @Test
    public void test_should_populate_attribute_values_field_in_service_request_from_domain_complaint() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
            .locationId("locationIdName")
            .coordinates(new Coordinates(1.0, 2.0, "tenantId"))
            .build();
        final Complainant complainant = Complainant.builder()
            .address("complainantAddress")
            .build();
        final ServiceRequest complaint = ServiceRequest.builder()
            .authenticatedUser(AuthenticatedUser.createAnonymousUser())
            .complaintType(new ComplaintType("name", "code", "tenantId"))
            .requester(complainant)
            .complaintLocation(complaintLocation)
            .receivingMode("receivingModeName")
            .complaintStatus("complaintStatusName")
            .receivingCenter("receivingCenterName")
            .complaintLocation(complaintLocation)
            .childLocation("childLocationIdName")
            .state("stateName")
            .assignee(2L)
            .department(1L)
            .citizenFeedback("citizenFeedback")
            .build();

        final org.egov.pgrrest.common.contract.ServiceRequest serviceRequest =
            new org.egov.pgrrest.common.contract.ServiceRequest(complaint);

        assertNotNull(serviceRequest);
        final List<AttributeEntry> attributeEntries = serviceRequest.getAttribValues();
        assertEquals("receivingMode", attributeEntries.get(0).getKey());
        assertEquals("receivingModeName", attributeEntries.get(0).getName());
        assertEquals("complaintStatus", attributeEntries.get(1).getKey());
        assertEquals("complaintStatusName", attributeEntries.get(1).getName());
        assertEquals("receivingCenter", attributeEntries.get(2).getKey());
        assertEquals("receivingCenterName", attributeEntries.get(2).getName());
        assertEquals("locationId", attributeEntries.get(3).getKey());
        assertEquals("locationIdName", attributeEntries.get(3).getName());
        assertEquals("childLocationId", attributeEntries.get(4).getKey());
        assertEquals("childLocationIdName", attributeEntries.get(4).getName());
        assertEquals("stateId", attributeEntries.get(5).getKey());
        assertEquals("stateName", attributeEntries.get(5).getName());
        assertEquals("assigneeId", attributeEntries.get(6).getKey());
        assertEquals("2", attributeEntries.get(6).getName());
        assertEquals("departmentId", attributeEntries.get(7).getKey());
        assertEquals("1", attributeEntries.get(7).getName());
        assertEquals("citizenFeedback", attributeEntries.get(8).getKey());
        assertEquals("citizenFeedback", attributeEntries.get(8).getName());
    }

    @Test
    public void test_should_populate_values_field_in_service_request_from_domain_complaint() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
            .locationId("locationId")
            .coordinates(new Coordinates(1.0, 2.0, "tenantId"))
            .build();
        final Complainant complainant = Complainant.builder()
            .address("complainantAddress")
            .build();
        final ServiceRequest complaint = ServiceRequest.builder()
            .authenticatedUser(AuthenticatedUser.createAnonymousUser())
            .complaintType(new ComplaintType("name", "code", "tenantId"))
            .requester(complainant)
            .complaintLocation(complaintLocation)
            .receivingMode("receivingMode")
            .complaintStatus("complaintStatus")
            .receivingCenter("receivingCenter")
            .complaintLocation(complaintLocation)
            .childLocation("childLocation")
            .state("state")
            .assignee(2L)
            .department(1L)
            .citizenFeedback("citizenFeedback")
            .build();

        final org.egov.pgrrest.common.contract.ServiceRequest serviceRequest = new org.egov.pgrrest.common.contract.ServiceRequest(complaint);

        assertNotNull(serviceRequest);
        final Map<String, String> values = serviceRequest.getValues();
        assertEquals("receivingMode", values.get("receivingMode"));
        assertEquals("complaintStatus", values.get("complaintStatus"));
        assertEquals("receivingCenter", values.get("receivingCenter"));
        assertEquals("locationId", values.get("locationId"));
        assertEquals("childLocation", values.get("childLocationId"));
        assertEquals("state", values.get("stateId"));
        assertEquals("2", values.get("assigneeId"));
        assertEquals("1", values.get("departmentId"));
        assertEquals("citizenFeedback", values.get("citizenFeedback"));
    }

    @Test
    public void test_should_populate_values_field_with_non_null_values_from_domain_complaint() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
            .coordinates(new Coordinates(1.0, 2.0, "tenantId"))
            .build();
        final Complainant complainant = Complainant.builder()
            .address("complainantAddress")
            .build();
        final ServiceRequest complaint = ServiceRequest.builder()
            .authenticatedUser(AuthenticatedUser.createAnonymousUser())
            .complaintType(new ComplaintType("name", "code", "tenantId"))
            .requester(complainant)
            .receivingMode("receivingMode")
            .complaintStatus("complaintStatus")
            .complaintLocation(complaintLocation)
            .build();

        final org.egov.pgrrest.common.contract.ServiceRequest serviceRequest = new org.egov.pgrrest.common.contract.ServiceRequest(complaint);

        final Map<String, String> values = serviceRequest.getValues();
        final HashSet<String> expectedKeys = new HashSet<>();
        expectedKeys.add("receivingMode");
        expectedKeys.add("complaintStatus");
        assertEquals(expectedKeys, values.keySet());
    }

    @Test
    public void test_should_populate_attribute_values_field_with_non_null_values_from_domain_complaint() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
            .coordinates(new Coordinates(1.0, 2.0, "tenantId"))
            .build();
        final Complainant complainant = Complainant.builder()
            .address("complainantAddress")
            .build();
        final ServiceRequest complaint = ServiceRequest.builder()
            .authenticatedUser(AuthenticatedUser.createAnonymousUser())
            .complaintType(new ComplaintType("name", "code", "tenantId"))
            .requester(complainant)
            .receivingMode("receivingMode")
            .complaintStatus("complaintStatus")
            .complaintLocation(complaintLocation)
            .build();

        final org.egov.pgrrest.common.contract.ServiceRequest serviceRequest =
            new org.egov.pgrrest.common.contract.ServiceRequest(complaint);

        final List<AttributeEntry> attributeEntries = serviceRequest.getAttribValues();
        assertEquals(2, attributeEntries.size());
        assertEquals("receivingMode", attributeEntries.get(0).getKey());
        assertEquals("complaintStatus", attributeEntries.get(1).getKey());
    }

    private AuthenticatedUser getAuthenticatedUser() {
        return AuthenticatedUser.builder()
            .id(1L)
            .type(UserType.CITIZEN)
            .build();
    }

}