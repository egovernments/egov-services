package org.egov.pgr.domain.model;

import org.egov.pgr.domain.exception.InvalidComplaintException;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.fail;

public class ComplaintTest {

    @Test
    public void testAtleastLatLngShouldBeProvidedToResolveLocation() throws Exception {
        try {
            Complaint complaint = getComplaintWithLatLngOnly();
            complaint.validate();
        } catch (InvalidComplaintException e) {
            fail("Expected complaint to be valid.");
        }
    }

    @Test
    public void testAtleastCrossHierarchyIdShouldBeProvidedToResolveLocation() throws Exception {
        try {
            Complaint complaint = getComplaintWithCrsHierarchyIdOnly();
            complaint.validate();
        } catch (InvalidComplaintException e) {
            fail("Expected complaint to be valid.");
        }
    }

    @Test
    public void testAtleastLocationIdShouldBeProvidedToResolveLocation() throws Exception {
        try {
            Complaint complaint = getComplaintWithLocationIdOnly();
            complaint.validate();
        } catch (InvalidComplaintException e) {
            fail("Expected complaint to be valid.");
        }
    }

    @Test(expected = InvalidComplaintException.class)
    public void testThatExceptionIsRaisedWhenNoLocationDataIsPresent() throws Exception {
        Complaint complaint = getComplaintWithNoLocationData();
        complaint.validate();
    }

    private Complaint getComplaintWithLatLngOnly() {
        return getComplaint(new ComplaintLocation(new Coordinates(12.22d, 11.22d), null, null));
    }

    private Complaint getComplaintWithCrsHierarchyIdOnly() {
        return getComplaint(new ComplaintLocation(new Coordinates(0d, 0d), "id", null));
    }

    private Complaint getComplaintWithLocationIdOnly() {
        return getComplaint(new ComplaintLocation(new Coordinates(0d, 0d), null, "12"));
    }

    private Complaint getComplaintWithNoLocationData() {
        return getComplaint(new ComplaintLocation(new Coordinates(0d, 0d), null, null));
    }

    private Complaint getComplaint(ComplaintLocation complaintLocation) {
        AuthenticatedUser user = getAuthenticatedUser();
        return Complaint.builder()
                .complainant(Complainant.builder().build())
                .authenticatedUser(user)
                .complaintLocation(complaintLocation)
                .build();
    }

    private AuthenticatedUser getAuthenticatedUser() {
        return AuthenticatedUser.builder()
                .id(1)
                .type(Collections.singletonList(UserType.CITIZEN))
                .build();
    }
}