package org.egov.pgrrest.read.domain.model;

import org.egov.pgrrest.common.model.AuthenticatedUser;
import org.egov.pgrrest.common.model.Complainant;
import org.egov.pgrrest.common.model.UserType;
import org.egov.pgrrest.read.domain.exception.InvalidComplaintException;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ServiceRequestTest {

    @Test
    public void testShouldNotFailValidationWhenCitizenCreatesValidComplaint() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
                .coordinates(new Coordinates(12.22d, 11.22d, "tenantId"))
                .build();
        final Complainant complainant = Complainant.builder()
            .userId("userId")
            .mobile("mobile number")
            .firstName("first name")
            .build();
        ServiceRequest complaint = ServiceRequest.builder()
            .requester(complainant)
            .authenticatedUser(getCitizen())
            .complaintLocation(complaintLocation)
            .tenantId("tenantId")
            .description("description")
            .complaintType(new ComplaintType(null, "complaintCode", "tenantId"))
            .build();

        complaint.validate();
    }

    @Test
    public void testShouldNotFailValidationWhenCitizenUpdatesComplaintWithAllValidAttributes() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
            .locationId("locationId")
            .build();
        final Complainant complainant = Complainant.builder()
            .userId("userId")
            .mobile("mobile number")
            .firstName("first name")
            .build();
        ServiceRequest complaint = ServiceRequest.builder()
            .requester(complainant)
            .authenticatedUser(getCitizen())
            .complaintLocation(complaintLocation)
            .tenantId("tenantId")
            .description("description")
            .crn("crn")
            .complaintType(new ComplaintType(null, "complaintCode", "tenantId"))
            .build();

        complaint.validate();
    }

    @Test(expected = InvalidComplaintException.class)
    public void testShouldFailValidationWhenLocationIdIsNotProvidedOnUpdatingComplaint() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
            .locationId(null)
            .build();
        ServiceRequest complaint = ServiceRequest.builder()
            .requester(Complainant.builder().build())
            .authenticatedUser(getCitizen())
            .complaintLocation(complaintLocation)
            .modifyServiceRequest(true)
            .complaintType(new ComplaintType(null, null, "tenantId"))
            .build();

        assertTrue(complaint.isLocationAbsent());
        assertTrue(complaint.isLocationIdAbsent());
        complaint.validate();
    }

    @Test(expected = InvalidComplaintException.class)
    public void testShouldFailValidationWhenNeitherCrossHierarchyIdNorCoordinatesArePresentOnCreatingComplaint() {
        ServiceRequest complaint = getComplaintWithNoLocationData();

        assertTrue(complaint.isLocationAbsent());
        assertTrue(complaint.isRawLocationAbsent());
        complaint.validate();
    }

    @Test(expected = InvalidComplaintException.class)
    public void testShouldFailValidationWhenFirstNameIsNotPresentWhenCreatingAnAnonymousComplaint() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
            .coordinates(new Coordinates(1.1, 2.2, "tenantId"))
            .build();
        final Complainant complainant = Complainant.builder()
            .mobile("mobile number")
            .email("foo@bar.com")
            .firstName(null)
            .build();
        ServiceRequest complaint = ServiceRequest.builder()
            .requester(complainant)
            .authenticatedUser(AuthenticatedUser.createAnonymousUser())
            .complaintLocation(complaintLocation)
            .complaintType(new ComplaintType(null, null, "tenantId"))
            .build();
        assertTrue(complaint.isRequesterAbsent());
        complaint.validate();
    }

    @Test(expected = InvalidComplaintException.class)
    public void testShouldFailValidationWhenFirstNameIsNotPresentWhenEmployeeCreatesComplaint() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
            .coordinates(new Coordinates(1.1, 2.2, "tenantId"))
            .build();
        final Complainant complainant = Complainant.builder()
            .mobile("mobile number")
            .email("foo@bar.com")
            .firstName(null)
            .build();
        ServiceRequest complaint = ServiceRequest.builder()
            .requester(complainant)
            .authenticatedUser(getEmployee())
            .complaintLocation(complaintLocation)
            .complaintType(new ComplaintType(null, null, "tenantId"))
            .build();
        assertTrue(complaint.isRequesterAbsent());
        complaint.validate();
    }

    @Test(expected = InvalidComplaintException.class)
    public void testShouldFailValidationWhenMobileNumberIsNotPresentWhenCreatingAnAnonymousComplaint() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
            .coordinates(new Coordinates(1.1, 2.2, "tenantId"))
            .build();
        final Complainant complainant = Complainant.builder()
            .firstName("first name")
            .email("foo@bar.com")
            .mobile(null)
            .build();
        ServiceRequest complaint = ServiceRequest.builder()
            .requester(complainant)
            .authenticatedUser(AuthenticatedUser.createAnonymousUser())
            .complaintLocation(complaintLocation)
            .complaintType(new ComplaintType(null, null, "tenantId"))
            .build();

        assertTrue(complaint.isRequesterAbsent());
        complaint.validate();
    }

    @Test(expected = InvalidComplaintException.class)
    public void testShouldFailValidationWhenMobileNumberIsNotPresentWhenEmployeeCreatesComplaint() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
            .coordinates(new Coordinates(1.1, 2.2, "tenantId"))
            .build();
        final Complainant complainant = Complainant.builder()
            .firstName("first name")
            .email("foo@bar.com")
            .mobile(null)
            .build();
        ServiceRequest complaint = ServiceRequest.builder()
                .requester(complainant)
                .authenticatedUser(getEmployee())
                .complaintLocation(complaintLocation)
                .complaintType(new ComplaintType(null, null, "tenantId"))
                .build();

        assertTrue(complaint.isRequesterAbsent());
        assertTrue(complaint.isComplainantPhoneAbsent());
        complaint.validate();
    }

    @Test
    public void testShouldNotFailValidationWhenMandatoryComplainantAttributesArePresentWhenCreatingAnAnonymousComplaint() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
                .coordinates(new Coordinates(1.1, 2.2, "tenantId"))
                .build();
        final Complainant complainant = Complainant.builder()
            .mobile("mobile number")
            .email("foo@bar.com")
            .firstName("first name")
            .build();
        ServiceRequest complaint = ServiceRequest.builder()
                .requester(complainant)
                .authenticatedUser(AuthenticatedUser.createAnonymousUser())
                .complaintLocation(complaintLocation)
                .tenantId("tenantId")
                .description("description")
                .complaintType(new ComplaintType(null, "complaintCode", "tenantId"))
                .build();

        assertFalse(complaint.isRequesterAbsent());
        complaint.validate();
    }

    @Test
    public void testShouldNotFailValidationWhenMandatoryComplainantAttributesArePresentOnComplaintCreationByEmployee() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
                .coordinates(new Coordinates(1.1, 2.2, "tenantId"))
                .build();
        final Complainant complainant = Complainant.builder()
            .mobile("mobile number")
            .email("foo@bar.com")
            .firstName("first name")
            .build();
        ServiceRequest complaint = ServiceRequest.builder()
                .requester(complainant)
                .authenticatedUser(getEmployee())
                .complaintLocation(complaintLocation)
                .tenantId("tenantId")
                .description("description")
                .receivingMode("receivingMode")
                .complaintType(new ComplaintType(null, "complaintCode", "tenantId"))
                .build();

        complaint.validate();
    }

    @Test(expected = InvalidComplaintException.class)
    public void testShouldFailValidationWhenTenantIdIsAbsentWhenCreatingComplaint() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
                .coordinates(new Coordinates(1.1, 2.2, "tenantId"))
                .build();
        final Complainant complainant = Complainant.builder()
            .mobile("mobile number")
            .email("foo@bar.com")
            .firstName("first name")
            .build();
        ServiceRequest complaint = ServiceRequest.builder()
                .requester(complainant)
                .tenantId(null)
                .authenticatedUser(AuthenticatedUser.createAnonymousUser())
                .complaintLocation(complaintLocation)
                .description("description")
                .complaintType(new ComplaintType(null, "complaintCode", "tenantId"))
                .build();

        assertTrue(complaint.isTenantIdAbsent());
        complaint.validate();
    }

    @Test(expected = InvalidComplaintException.class)
    public void testShouldFailValidationWhenDescriptionIsAbsentWhenCreatingComplaint() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
                .coordinates(new Coordinates(1.1, 2.2, "tenantId"))
                .build();
        final Complainant complainant = Complainant.builder()
            .mobile("mobile number")
            .email("foo@bar.com")
            .firstName("first name")
            .build();
        ServiceRequest complaint = ServiceRequest.builder()
                .requester(complainant)
                .tenantId("tenantId")
                .description(null)
                .authenticatedUser(AuthenticatedUser.createAnonymousUser())
                .complaintLocation(complaintLocation)
                .complaintType(new ComplaintType(null, null, "tenantId"))
                .build();

        assertTrue(complaint.isDescriptionAbsent());
        complaint.validate();
    }
    
    @Test(expected = InvalidComplaintException.class)
    public void testShouldFailValidationWhenDescriptionLengthIsLessWhenCreatingComplaint() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
            .coordinates(new Coordinates(1.1, 2.2,"ap.public"))
            .build();
        final Complainant complainant = Complainant.builder()
            .mobile("mobile number")
            .email("foo@bar.com")
            .firstName("first name")
            .build();
        ServiceRequest complaint = ServiceRequest.builder()
            .requester(complainant)
            .tenantId("tenantId")
            .description("description")
            .authenticatedUser(AuthenticatedUser.createAnonymousUser())
            .complaintLocation(complaintLocation)
            .complaintType(new ComplaintType(null, null, null)) 
            .build();

		assertFalse(complaint.descriptionLength());
		complaint.validate();
    }

    @Test(expected = InvalidComplaintException.class)
    public void testShouldFailValidationWhenComplaintTypeCodeIsAbsentWhenCreatingComplaint() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
                .coordinates(new Coordinates(1.1, 2.2, "tenantId"))
                .build();
        final Complainant complainant = Complainant.builder()
            .mobile("mobile number")
            .email("foo@bar.com")
            .firstName("first name")
            .build();
        ServiceRequest complaint = ServiceRequest.builder()
                .requester(complainant)
                .tenantId("tenantId")
                .description("description")
                .complaintType(new ComplaintType("type", null, "tenantId"))
                .authenticatedUser(AuthenticatedUser.createAnonymousUser())
                .complaintLocation(complaintLocation)
                .build();

        assertTrue(complaint.isComplaintTypeAbsent());
        complaint.validate();
    }

    @Test(expected = InvalidComplaintException.class)
    public void testShouldFailValidationWhenCrnIsAbsentWhenUpdatingComplaint() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
                .coordinates(new Coordinates(1.1, 2.2, "tenantId"))
                .build();
        final Complainant complainant = Complainant.builder()
            .mobile("mobile number")
            .email("foo@bar.com")
            .firstName("first name")
            .build();
        ServiceRequest complaint = ServiceRequest.builder()
                .requester(complainant)
                .tenantId("tenantId")
                .description("description")
                .complaintType(new ComplaintType("type", "complaintTypeCode", "tenantId"))
                .crn(null)
                .modifyServiceRequest(true)
                .authenticatedUser(AuthenticatedUser.createAnonymousUser())
                .complaintLocation(complaintLocation)
                .build();

        assertTrue(complaint.isCrnAbsent());
        complaint.validate();
    }

    private ServiceRequest getComplaintWithNoLocationData() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
                .coordinates(new Coordinates(0d, 0d, "tenantId"))
                .build();
        return getComplaint(complaintLocation);
    }

    private ServiceRequest getComplaint(ComplaintLocation complaintLocation) {
        return ServiceRequest.builder()
                .requester(Complainant.builder().build())
                .authenticatedUser(getCitizen())
                .complaintLocation(complaintLocation)
                .tenantId("tenantId")
                .description("description")
                .complaintType(new ComplaintType(null, "complaintTypeCode", "tenantId"))
                .build();
    }

    private AuthenticatedUser getCitizen() {
        return AuthenticatedUser.builder()
            .id(1L)
            .type(UserType.CITIZEN)
            .build();
    }

    private AuthenticatedUser getEmployee() {
        return AuthenticatedUser.builder()
            .id(1L)
            .type(UserType.EMPLOYEE)
            .build();
    }
}