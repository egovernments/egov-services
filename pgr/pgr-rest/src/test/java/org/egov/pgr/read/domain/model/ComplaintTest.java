package org.egov.pgr.read.domain.model;

import org.egov.pgr.common.model.AuthenticatedUser;
import org.egov.pgr.common.model.Complainant;
import org.egov.pgr.common.model.UserType;
import org.egov.pgr.read.domain.exception.InvalidComplaintException;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ComplaintTest {

    @Test
    public void test_should_not_fail_validation_when_citizen_creates_a_valid_complaint() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
            .coordinates(new Coordinates(12.22d, 11.22d))
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
            .complaintType(new ComplaintType(null, "complaintCode"))
            .build();

        complaint.validate();
    }

    @Test
    public void test_should_not_fail_validation_when_citizen_updates_a_complaint_with_all_valid_attributes() {
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
            .complaintType(new ComplaintType(null, "complaintCode"))
            .build();

        complaint.validate();
    }

    @Test(expected = InvalidComplaintException.class)
    public void test_should_fail_validation_when_location_id_is_not_provided_on_updating_a_complaint() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
            .locationId(null)
            .build();
        Complaint complaint = Complaint.builder()
            .complainant(Complainant.builder().build())
            .authenticatedUser(getCitizen())
            .complaintLocation(complaintLocation)
            .modifyComplaint(true)
            .complaintType(new ComplaintType(null, null))
            .build();

        assertTrue(complaint.isLocationAbsent());
        assertTrue(complaint.isLocationIdAbsent());
        complaint.validate();
    }

    @Test(expected = InvalidComplaintException.class)
    public void
    test_should_fail_validation_when_neither_cross_hierarchy_id_nor_coordinates_are_present_on_creating_a_complaint() {
        Complaint complaint = getComplaintWithNoLocationData();

        assertTrue(complaint.isLocationAbsent());
        assertTrue(complaint.isRawLocationAbsent());
        complaint.validate();
    }

    @Test(expected = InvalidComplaintException.class)
    public void test_should_fail_validation_when_first_name_is_not_present_when_creating_an_anonymous_complaint() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
            .coordinates(new Coordinates(1.1, 2.2))
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
            .complaintType(new ComplaintType(null, null))
            .build();

        assertTrue(complaint.isComplainantAbsent());
        complaint.validate();
    }

    @Test(expected = InvalidComplaintException.class)
    public void test_should_fail_validation_when_first_name_is_not_present_when_employee_creates_a_complaint() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
            .coordinates(new Coordinates(1.1, 2.2))
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
            .complaintType(new ComplaintType(null, null))
            .build();

        assertTrue(complaint.isComplainantAbsent());
        complaint.validate();
    }

    @Test(expected = InvalidComplaintException.class)
    public void test_should_fail_validation_when_mobile_number_is_not_present_when_creating_an_anonymous_complaint() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
            .coordinates(new Coordinates(1.1, 2.2))
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
            .complaintType(new ComplaintType(null, null))
            .build();

        assertTrue(complaint.isComplainantAbsent());
        complaint.validate();
    }

    @Test(expected = InvalidComplaintException.class)
    public void test_should_fail_validation_when_mobile_number_is_not_present_when_employee_creates_a_complaint() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
            .coordinates(new Coordinates(1.1, 2.2))
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
            .complaintType(new ComplaintType(null, null))
            .build();

        assertTrue(complaint.isComplainantAbsent());
        assertTrue(complaint.isComplainantPhoneAbsent());
        complaint.validate();
    }

    @Test
    public void
    test_should_not_fail_validation_when_mandatory_complainant_attributes_are_present_when_creating_an_anonymous_complaint() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
            .coordinates(new Coordinates(1.1, 2.2))
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
            .complaintType(new ComplaintType(null, "complaintCode"))
            .build();

        assertFalse(complaint.isComplainantAbsent());
        complaint.validate();
    }

    @Test
    public void
    test_should_not_fail_validation_when_mandatory_complainant_attributes_are_present_on_complaint_creation_by_employee() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
            .coordinates(new Coordinates(1.1, 2.2))
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
            .complaintType(new ComplaintType(null, "complaintCode"))
            .build();

        complaint.validate();
    }

    @Test(expected = InvalidComplaintException.class)
    public void test_should_fail_validation_when_tenant_id_is_absent_when_creating_a_complaint() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
            .coordinates(new Coordinates(1.1, 2.2))
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
            .complaintType(new ComplaintType(null, "complaintCode"))
            .build();

        assertTrue(complaint.isTenantIdAbsent());
        complaint.validate();
    }

    @Test(expected = InvalidComplaintException.class)
    public void test_should_fail_validation_when_description_is_absent_when_creating_a_complaint() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
            .coordinates(new Coordinates(1.1, 2.2))
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
            .complaintType(new ComplaintType(null, null))
            .build();

        assertTrue(complaint.isDescriptionAbsent());
        complaint.validate();
    }

    @Test(expected = InvalidComplaintException.class)
    public void test_should_fail_validation_when_complaint_type_code_is_absent_when_creating_a_complaint() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
            .coordinates(new Coordinates(1.1, 2.2))
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
            .complaintType(new ComplaintType("type", null))
            .authenticatedUser(AuthenticatedUser.createAnonymousUser())
            .complaintLocation(complaintLocation)
            .build();

        assertTrue(complaint.isComplaintTypeAbsent());
        complaint.validate();
    }

    @Test(expected = InvalidComplaintException.class)
    public void test_should_fail_validation_when_crn_is_absent_when_updating_a_complaint() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
            .coordinates(new Coordinates(1.1, 2.2))
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
            .complaintType(new ComplaintType("type", "complaintTypeCode"))
            .crn(null)
            .modifyComplaint(true)
            .authenticatedUser(AuthenticatedUser.createAnonymousUser())
            .complaintLocation(complaintLocation)
            .build();

        assertTrue(complaint.isCrnAbsent());
        complaint.validate();
    }

    @Test(expected = InvalidComplaintException.class)
    public void test_should_fail_validation_when_receiving_mode_is_not_present_when_employee_creates_a_complaint() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
            .coordinates(new Coordinates(1.1, 2.2))
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
            .complaintType(new ComplaintType("type", "complaintTypeCode"))
            .receivingCenter(null)
            .receivingMode(null)
            .authenticatedUser(getEmployee())
            .complaintLocation(complaintLocation)
            .build();

        assertTrue(complaint.isReceivingModeAbsent());
        complaint.validate();
    }

    @Test(expected = InvalidComplaintException.class)
    public void
    test_should_fail_validation_when_receiving_center_is_not_present_when_employee_creates_a_complaint_with_manual_receiving_mode() {
        final ComplaintLocation complaintLocation = ComplaintLocation.builder()
            .coordinates(new Coordinates(1.1, 2.2))
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
            .complaintType(new ComplaintType("type", "complaintTypeCode"))
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
            .coordinates(new Coordinates(0d, 0d))
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
            .complaintType(new ComplaintType(null, "complaintTypeCode"))
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