package org.egov.pgrrest.read.domain.model;

import org.egov.pgrrest.common.model.AuthenticatedUser;
import org.egov.pgrrest.common.model.Complainant;
import org.egov.pgrrest.common.model.UserType;
import org.egov.pgrrest.read.domain.exception.InvalidComplaintException;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ComplaintTest {

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
        Complaint complaint = Complaint.builder()
            .complainant(complainant)
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
        Complaint complaint = Complaint.builder()
            .complainant(complainant)
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
        Complaint complaint = Complaint.builder()
            .complainant(Complainant.builder().build())
            .authenticatedUser(getCitizen())
            .complaintLocation(complaintLocation)
            .modifyComplaint(true)
            .complaintType(new ComplaintType(null, null, "tenantId"))
            .build();

        assertTrue(complaint.isLocationAbsent());
        assertTrue(complaint.isLocationIdAbsent());
        complaint.validate();
    }

    @Test(expected = InvalidComplaintException.class)
    public void testShouldFailValidationWhenNeitherCrossHierarchyIdNorCoordinatesArePresentOnCreatingComplaint() {
        Complaint complaint = getComplaintWithNoLocationData();

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
        Complaint complaint = Complaint.builder()
            .complainant(complainant)
            .authenticatedUser(AuthenticatedUser.createAnonymousUser())
            .complaintLocation(complaintLocation)
            .complaintType(new ComplaintType(null, null, "tenantId"))
            .build();
        assertTrue(complaint.isComplainantAbsent());
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
        Complaint complaint = Complaint.builder()
            .complainant(complainant)
            .authenticatedUser(getEmployee())
            .complaintLocation(complaintLocation)
            .complaintType(new ComplaintType(null, null, "tenantId"))
            .build();
        assertTrue(complaint.isComplainantAbsent());
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
        Complaint complaint = Complaint.builder()
            .complainant(complainant)
            .authenticatedUser(AuthenticatedUser.createAnonymousUser())
            .complaintLocation(complaintLocation)
            .complaintType(new ComplaintType(null, null, "tenantId"))
            .build();

        assertTrue(complaint.isComplainantAbsent());
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
        Complaint complaint = Complaint.builder()
                .complainant(complainant)
                .authenticatedUser(getEmployee())
                .complaintLocation(complaintLocation)
                .complaintType(new ComplaintType(null, null, "tenantId"))
                .build();

        assertTrue(complaint.isComplainantAbsent());
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
        Complaint complaint = Complaint.builder()
                .complainant(complainant)
                .authenticatedUser(AuthenticatedUser.createAnonymousUser())
                .complaintLocation(complaintLocation)
                .tenantId("tenantId")
                .description("description")
                .complaintType(new ComplaintType(null, "complaintCode", "tenantId"))
                .build();

        assertFalse(complaint.isComplainantAbsent());
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
        Complaint complaint = Complaint.builder()
                .complainant(complainant)
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
        Complaint complaint = Complaint.builder()
                .complainant(complainant)
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
        Complaint complaint = Complaint.builder()
                .complainant(complainant)
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
        Complaint complaint = Complaint.builder()
            .complainant(complainant)
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
        Complaint complaint = Complaint.builder()
                .complainant(complainant)
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
        Complaint complaint = Complaint.builder()
                .complainant(complainant)
                .tenantId("tenantId")
                .description("description")
                .complaintType(new ComplaintType("type", "complaintTypeCode", "tenantId"))
                .crn(null)
                .modifyComplaint(true)
                .authenticatedUser(AuthenticatedUser.createAnonymousUser())
                .complaintLocation(complaintLocation)
                .build();

        assertTrue(complaint.isCrnAbsent());
        complaint.validate();
    }

    @Test(expected = InvalidComplaintException.class)
    public void testShouldFailValidationWhenReceivingModeIsNotPresentWhenEmployeeCreatesComplaint() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
                .coordinates(new Coordinates(1.1, 2.2, "tenantId"))
                .build();
        final Complainant complainant = Complainant.builder()
            .mobile("mobile number")
            .email("foo@bar.com")
            .firstName("first name")
            .build();
        Complaint complaint = Complaint.builder()
                .complainant(complainant)
                .tenantId("tenantId")
                .description("description")
                .complaintType(new ComplaintType("type", "complaintTypeCode", "tenantId"))
                .receivingCenter(null)
                .receivingMode(null)
                .authenticatedUser(getEmployee())
                .complaintLocation(complaintLocation)
                .build();

        assertTrue(complaint.isReceivingModeAbsent());
        complaint.validate();
    }

    @Test(expected = InvalidComplaintException.class)
    public void testShouldFailValidationWhenReceivingCenterIsNotPresentWhenEmployeeCreatesComplaintWithManualReceivingMode() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
                .coordinates(new Coordinates(1.1, 2.2, "tenantId"))
                .build();
        final Complainant complainant = Complainant.builder()
            .mobile("mobile number")
            .email("foo@bar.com")
            .firstName("first name")
            .build();
        Complaint complaint = Complaint.builder()
                .complainant(complainant)
                .tenantId("tenantId")
                .description("description")
                .complaintType(new ComplaintType("type", "complaintTypeCode", "tenantId"))
                .receivingCenter(null)
                .receivingMode("MANUAL")
                .authenticatedUser(getEmployee())
                .complaintLocation(complaintLocation)
                .build();

        assertTrue(complaint.isReceivingCenterAbsent());
        complaint.validate();
    }


    private Complaint getComplaintWithNoLocationData() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
                .coordinates(new Coordinates(0d, 0d, "tenantId"))
                .build();
        return getComplaint(complaintLocation);
    }

    private Complaint getComplaint(ComplaintLocation complaintLocation) {
        return Complaint.builder()
                .complainant(Complainant.builder().build())
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