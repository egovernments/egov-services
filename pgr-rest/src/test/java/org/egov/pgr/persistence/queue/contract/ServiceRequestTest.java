package org.egov.pgr.persistence.queue.contract;

import org.apache.commons.lang3.StringUtils;
import org.egov.pgr.domain.model.AuthenticatedUser;
import org.egov.pgr.domain.model.Complaint;
import org.egov.pgr.domain.model.UserType;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ServiceRequestTest {

    private String tenantId = "some.ulb";

    @Test
    public void testLocationIdIsCopiedOverToComplaint() throws Exception {
        String expectedLocationId = "12";
        ServiceRequest serviceRequest = getServiceRequestWithLocationId(expectedLocationId);
        Complaint complaint = serviceRequest.toDomain(getAuthenticatedUser(), tenantId);
        assertEquals(expectedLocationId, complaint.getComplaintLocation().getLocationId());
    }

    @Test
    public void testLocationCoordinatesAreCopiedOverToComplaint() throws Exception {
        Double lat = 12.343, lng = 23.243;
        ServiceRequest serviceRequest = getServiceRequestWithCoordinates(lat, lng);
        Complaint complaint = serviceRequest.toDomain(getAuthenticatedUser(), tenantId);
        assertEquals(lat, complaint.getComplaintLocation().getCoordinates().getLatitude());
        assertEquals(lng, complaint.getComplaintLocation().getCoordinates().getLongitude());
    }

    @Test
    public void testCrossHierarchyIdIsCopiedOverToComplaint() throws Exception {
        String crossHierarchyId = "12";
        ServiceRequest serviceRequest = getServiceRequestWithCrossHierarchyId(crossHierarchyId);
        Complaint complaint = serviceRequest.toDomain(getAuthenticatedUser(), tenantId);
        assertEquals(crossHierarchyId, complaint.getComplaintLocation().getCrossHierarchyId());
    }

    @Test
    public void testLocationValuesShouldBeNullWhenNotProvided() throws Exception {
        ServiceRequest serviceRequest = getServiceIdWithoutLocationValues();
        Complaint complaint = serviceRequest.toDomain(getAuthenticatedUser(), tenantId);
        assertNull(complaint.getComplaintLocation().getCrossHierarchyId());
        assertNull(complaint.getComplaintLocation().getCoordinates().getLatitude());
        assertNull(complaint.getComplaintLocation().getCoordinates().getLongitude());
        assertEquals(StringUtils.EMPTY, complaint.getComplaintLocation().getLocationId());
    }

    private AuthenticatedUser getAuthenticatedUser() {
        return AuthenticatedUser.builder()
                .id(1)
                .type(Collections.singletonList(UserType.CITIZEN))
                .build();
    }

    private ServiceRequest getServiceRequestWithCoordinates(Double lat, Double lng) {
        return ServiceRequest.builder().latitude(lat).longitude(lng).values(new HashMap<>()).build();
    }

    private ServiceRequest getServiceRequestWithCrossHierarchyId(String crossHierarchyId) {
        return ServiceRequest.builder().crossHierarchyId(crossHierarchyId).values(new HashMap<>()).build();
    }

    private ServiceRequest getServiceIdWithoutLocationValues() {
        return ServiceRequest.builder().values(new HashMap<>()).build();
    }

    private ServiceRequest getServiceRequestWithLocationId(String locationId) {
        Map<String, String> values = new HashMap<>();
        values.put("location_id", locationId);
        return ServiceRequest.builder().values(values).build();
    }

}