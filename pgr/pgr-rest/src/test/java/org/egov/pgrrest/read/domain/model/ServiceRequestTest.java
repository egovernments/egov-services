package org.egov.pgrrest.read.domain.model;

import org.egov.pgrrest.common.model.AuthenticatedUser;
import org.egov.pgrrest.common.model.Requester;
import org.egov.pgrrest.common.model.UserType;
import org.egov.pgrrest.read.domain.exception.InvalidComplaintException;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ServiceRequestTest {

    @Test
    public void testShouldNotFailValidationWhenCitizenCreatesValidComplaint() {
        final ServiceRequestLocation serviceRequestLocation = ServiceRequestLocation.builder()
                .coordinates(new Coordinates(12.22d, 11.22d))
                .build();
        final Requester complainant = Requester.builder()
            .userId("userId")
            .mobile("mobile number")
            .firstName("first name")
            .build();
        ServiceRequest complaint = ServiceRequest.builder()
            .requester(complainant)
            .authenticatedUser(getCitizen())
            .serviceRequestLocation(serviceRequestLocation)
            .tenantId("tenantId")
            .description("description")
            .complaintType(new ServiceRequestType(null, "complaintCode", "tenantId"))
            .build();

        complaint.validate();
    }

    @Test
    public void testShouldNotFailValidationWhenCitizenUpdatesComplaintWithAllValidAttributes() {
        final ServiceRequestLocation serviceRequestLocation = ServiceRequestLocation.builder()
            .locationId("locationId")
            .build();
        final Requester complainant = Requester.builder()
            .userId("userId")
            .mobile("mobile number")
            .firstName("first name")
            .build();
        ServiceRequest complaint = ServiceRequest.builder()
            .requester(complainant)
            .authenticatedUser(getCitizen())
            .serviceRequestLocation(serviceRequestLocation)
            .tenantId("tenantId")
            .description("description")
            .crn("crn")
            .complaintType(new ServiceRequestType(null, "complaintCode", "tenantId"))
            .build();

        complaint.validate();
    }

    @Test(expected = InvalidComplaintException.class)
    public void testShouldFailValidationWhenLocationIdIsNotProvidedOnUpdatingComplaint() {
        final ServiceRequestLocation serviceRequestLocation = ServiceRequestLocation.builder()
            .locationId(null)
            .build();
        ServiceRequest complaint = ServiceRequest.builder()
            .requester(Requester.builder().build())
            .authenticatedUser(getCitizen())
            .serviceRequestLocation(serviceRequestLocation)
            .modifyServiceRequest(true)
            .complaintType(new ServiceRequestType(null, null, "tenantId"))
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
        final ServiceRequestLocation serviceRequestLocation = ServiceRequestLocation.builder()
            .coordinates(new Coordinates(1.1, 2.2))
            .build();
        final Requester complainant = Requester.builder()
            .mobile("mobile number")
            .email("foo@bar.com")
            .firstName(null)
            .build();
        ServiceRequest complaint = ServiceRequest.builder()
            .requester(complainant)
            .authenticatedUser(AuthenticatedUser.createAnonymousUser())
            .serviceRequestLocation(serviceRequestLocation)
            .complaintType(new ServiceRequestType(null, null, "tenantId"))
            .build();
        assertTrue(complaint.isRequesterAbsent());
        complaint.validate();
    }

    @Test(expected = InvalidComplaintException.class)
    public void testShouldFailValidationWhenFirstNameIsNotPresentWhenEmployeeCreatesComplaint() {
        final ServiceRequestLocation serviceRequestLocation = ServiceRequestLocation.builder()
            .coordinates(new Coordinates(1.1, 2.2))
            .build();
        final Requester complainant = Requester.builder()
            .mobile("mobile number")
            .email("foo@bar.com")
            .firstName(null)
            .build();
        ServiceRequest complaint = ServiceRequest.builder()
            .requester(complainant)
            .authenticatedUser(getEmployee())
            .serviceRequestLocation(serviceRequestLocation)
            .complaintType(new ServiceRequestType(null, null, "tenantId"))
            .build();
        assertTrue(complaint.isRequesterAbsent());
        complaint.validate();
    }

    @Test(expected = InvalidComplaintException.class)
    public void testShouldFailValidationWhenMobileNumberIsNotPresentWhenCreatingAnAnonymousComplaint() {
        final ServiceRequestLocation serviceRequestLocation = ServiceRequestLocation.builder()
            .coordinates(new Coordinates(1.1, 2.2))
            .build();
        final Requester complainant = Requester.builder()
            .firstName("first name")
            .email("foo@bar.com")
            .mobile(null)
            .build();
        ServiceRequest complaint = ServiceRequest.builder()
            .requester(complainant)
            .authenticatedUser(AuthenticatedUser.createAnonymousUser())
            .serviceRequestLocation(serviceRequestLocation)
            .complaintType(new ServiceRequestType(null, null, "tenantId"))
            .build();

        assertTrue(complaint.isRequesterAbsent());
        complaint.validate();
    }

    @Test(expected = InvalidComplaintException.class)
    public void testShouldFailValidationWhenMobileNumberIsNotPresentWhenEmployeeCreatesComplaint() {
        final ServiceRequestLocation serviceRequestLocation = ServiceRequestLocation.builder()
            .coordinates(new Coordinates(1.1, 2.2))
            .build();
        final Requester complainant = Requester.builder()
            .firstName("first name")
            .email("foo@bar.com")
            .mobile(null)
            .build();
        ServiceRequest complaint = ServiceRequest.builder()
                .requester(complainant)
                .authenticatedUser(getEmployee())
                .serviceRequestLocation(serviceRequestLocation)
                .complaintType(new ServiceRequestType(null, null, "tenantId"))
                .build();

        assertTrue(complaint.isRequesterAbsent());
        assertTrue(complaint.isComplainantPhoneAbsent());
        complaint.validate();
    }

    @Test
    public void testShouldNotFailValidationWhenMandatoryComplainantAttributesArePresentWhenCreatingAnAnonymousComplaint() {
        final ServiceRequestLocation serviceRequestLocation = ServiceRequestLocation.builder()
                .coordinates(new Coordinates(1.1, 2.2))
                .build();
        final Requester complainant = Requester.builder()
            .mobile("mobile number")
            .email("foo@bar.com")
            .firstName("first name")
            .build();
        ServiceRequest complaint = ServiceRequest.builder()
                .requester(complainant)
                .authenticatedUser(AuthenticatedUser.createAnonymousUser())
                .serviceRequestLocation(serviceRequestLocation)
                .tenantId("tenantId")
                .description("description")
                .complaintType(new ServiceRequestType(null, "complaintCode", "tenantId"))
                .build();

        assertFalse(complaint.isRequesterAbsent());
        complaint.validate();
    }

    @Test
    public void testShouldNotFailValidationWhenMandatoryComplainantAttributesArePresentOnComplaintCreationByEmployee() {
        final ServiceRequestLocation serviceRequestLocation = ServiceRequestLocation.builder()
                .coordinates(new Coordinates(1.1, 2.2))
                .build();
        final Requester complainant = Requester.builder()
            .mobile("mobile number")
            .email("foo@bar.com")
            .firstName("first name")
            .build();
        ServiceRequest complaint = ServiceRequest.builder()
                .requester(complainant)
                .authenticatedUser(getEmployee())
                .serviceRequestLocation(serviceRequestLocation)
                .tenantId("tenantId")
                .description("description")
                .receivingMode("receivingMode")
                .complaintType(new ServiceRequestType(null, "complaintCode", "tenantId"))
                .build();

        complaint.validate();
    }

    @Test(expected = InvalidComplaintException.class)
    public void testShouldFailValidationWhenTenantIdIsAbsentWhenCreatingComplaint() {
        final ServiceRequestLocation serviceRequestLocation = ServiceRequestLocation.builder()
                .coordinates(new Coordinates(1.1, 2.2))
                .build();
        final Requester complainant = Requester.builder()
            .mobile("mobile number")
            .email("foo@bar.com")
            .firstName("first name")
            .build();
        ServiceRequest complaint = ServiceRequest.builder()
                .requester(complainant)
                .tenantId(null)
                .authenticatedUser(AuthenticatedUser.createAnonymousUser())
                .serviceRequestLocation(serviceRequestLocation)
                .description("description")
                .complaintType(new ServiceRequestType(null, "complaintCode", "tenantId"))
                .build();

        assertTrue(complaint.isTenantIdAbsent());
        complaint.validate();
    }

    @Test(expected = InvalidComplaintException.class)
    public void testShouldFailValidationWhenDescriptionIsAbsentWhenCreatingComplaint() {
        final ServiceRequestLocation serviceRequestLocation = ServiceRequestLocation.builder()
                .coordinates(new Coordinates(1.1, 2.2))
                .build();
        final Requester complainant = Requester.builder()
            .mobile("mobile number")
            .email("foo@bar.com")
            .firstName("first name")
            .build();
        ServiceRequest complaint = ServiceRequest.builder()
                .requester(complainant)
                .tenantId("tenantId")
                .description(null)
                .authenticatedUser(AuthenticatedUser.createAnonymousUser())
                .serviceRequestLocation(serviceRequestLocation)
                .complaintType(new ServiceRequestType(null, null, "tenantId"))
                .build();

        assertTrue(complaint.isDescriptionAbsent());
        complaint.validate();
    }
    
    @Test(expected = InvalidComplaintException.class)
    public void testShouldFailValidationWhenDescriptionLengthIsLessWhenCreatingComplaint() {
        final ServiceRequestLocation serviceRequestLocation = ServiceRequestLocation.builder()
            .coordinates(new Coordinates(1.1, 2.2))
            .build();
        final Requester complainant = Requester.builder()
            .mobile("mobile number")
            .email("foo@bar.com")
            .firstName("first name")
            .build();
        ServiceRequest complaint = ServiceRequest.builder()
            .requester(complainant)
            .tenantId("tenantId")
            .description("description")
            .authenticatedUser(AuthenticatedUser.createAnonymousUser())
            .serviceRequestLocation(serviceRequestLocation)
            .complaintType(new ServiceRequestType(null, null, null))
            .build();

		assertFalse(complaint.descriptionLength());
		complaint.validate();
    }

    @Test(expected = InvalidComplaintException.class)
    public void testShouldFailValidationWhenComplaintTypeCodeIsAbsentWhenCreatingComplaint() {
        final ServiceRequestLocation serviceRequestLocation = ServiceRequestLocation.builder()
                .coordinates(new Coordinates(1.1, 2.2))
                .build();
        final Requester complainant = Requester.builder()
            .mobile("mobile number")
            .email("foo@bar.com")
            .firstName("first name")
            .build();
        ServiceRequest complaint = ServiceRequest.builder()
                .requester(complainant)
                .tenantId("tenantId")
                .description("description")
                .complaintType(new ServiceRequestType("type", null, "tenantId"))
                .authenticatedUser(AuthenticatedUser.createAnonymousUser())
                .serviceRequestLocation(serviceRequestLocation)
                .build();

        assertTrue(complaint.isComplaintTypeAbsent());
        complaint.validate();
    }

    @Test(expected = InvalidComplaintException.class)
    public void testShouldFailValidationWhenCrnIsAbsentWhenUpdatingComplaint() {
        final ServiceRequestLocation serviceRequestLocation = ServiceRequestLocation.builder()
                .coordinates(new Coordinates(1.1, 2.2))
                .build();
        final Requester complainant = Requester.builder()
            .mobile("mobile number")
            .email("foo@bar.com")
            .firstName("first name")
            .build();
        ServiceRequest complaint = ServiceRequest.builder()
                .requester(complainant)
                .tenantId("tenantId")
                .description("description")
                .complaintType(new ServiceRequestType("type", "complaintTypeCode", "tenantId"))
                .crn(null)
                .modifyServiceRequest(true)
                .authenticatedUser(AuthenticatedUser.createAnonymousUser())
                .serviceRequestLocation(serviceRequestLocation)
                .build();

        assertTrue(complaint.isCrnAbsent());
        complaint.validate();
    }

    private ServiceRequest getComplaintWithNoLocationData() {
        final ServiceRequestLocation serviceRequestLocation = ServiceRequestLocation.builder()
                .coordinates(new Coordinates(0d, 0d))
                .build();
        return getComplaint(serviceRequestLocation);
    }

    private ServiceRequest getComplaint(ServiceRequestLocation serviceRequestLocation) {
        return ServiceRequest.builder()
                .requester(Requester.builder().build())
                .authenticatedUser(getCitizen())
                .serviceRequestLocation(serviceRequestLocation)
                .tenantId("tenantId")
                .description("description")
                .complaintType(new ServiceRequestType(null, "complaintTypeCode", "tenantId"))
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