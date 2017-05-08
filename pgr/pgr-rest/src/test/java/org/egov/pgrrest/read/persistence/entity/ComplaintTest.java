package org.egov.pgrrest.read.persistence.entity;

import org.egov.pgrrest.common.entity.*;
import org.egov.pgrrest.common.model.AuthenticatedUser;
import org.egov.pgrrest.read.domain.model.ComplaintLocation;
import org.egov.pgrrest.read.domain.model.Coordinates;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.*;

public class ComplaintTest {

    private static final String IST = "Asia/Calcutta";

    @Test
    public void test_should_map_from_entity_to_domain() {
        final ComplaintType complaintType = new ComplaintType();
        complaintType.setName("complaintName");
        complaintType.setCode("complaintCode");
        complaintType.setTenantId("tenantId");

        final Complainant complainant = Complainant.builder().id(2L).name("firstName").mobile("mobileNumber")
                .email("email@email.com").address("address").build();
        final LocalDateTime lastAccessedDateTime = LocalDateTime.of(2016, 1, 2, 3, 4, 5);
        final ReceivingCenter receivingCenter = ReceivingCenter.builder().id(4L).build();
        final ReceivingMode receivingMode = new ReceivingMode();
        receivingMode.setCode("EMAIL");
        final Complaint entityComplaint = Complaint.builder().crn("crn").details("complaint description")
                .complaintType(complaintType).receivingMode(receivingMode).status("FORWARDED")
                .complainant(complainant).latitude(1.0).longitude(2.0).crossHierarchyId(4L).location(3L).department(3L)
                .lastAccessedTime(toDate(lastAccessedDateTime)).landmarkDetails("landMark").receivingMode(receivingMode)
                .receivingCenter(receivingCenter).childLocation(5L).assignee(6L).stateId(7L).tenantId("tenantId").build();
        final LocalDateTime lastModifiedDateTime = LocalDateTime.of(2016, 2, 3, 3, 3, 3);
        final LocalDateTime createdDateTime = LocalDateTime.of(2016, 2, 1, 3, 3, 3);
        entityComplaint.setLastModifiedDate(toDate(lastModifiedDateTime));
        entityComplaint.setCreatedDate(toDate(createdDateTime));

        final org.egov.pgrrest.read.domain.model.Complaint domainComplaint = entityComplaint.toDomain();

        assertNotNull(domainComplaint);
        final ComplaintLocation complaintLocation = domainComplaint.getComplaintLocation();
        assertNotNull(complaintLocation);
        assertEquals("3", complaintLocation.getLocationId());
        assertEquals("4", complaintLocation.getCrossHierarchyId());
        assertEquals(new Coordinates(1.0, 2.0, "tenantId"), complaintLocation.getCoordinates());
        assertEquals(toDate(lastAccessedDateTime), domainComplaint.getLastAccessedTime());
        assertEquals(toDate(lastModifiedDateTime), domainComplaint.getLastModifiedDate());
        assertEquals(toDate(createdDateTime), domainComplaint.getCreatedDate());
        assertEquals("3", domainComplaint.getDepartment());
        assertFalse(domainComplaint.isClosed());
        assertEquals(Collections.emptyList(), domainComplaint.getMediaUrls());
        assertEquals("crn", domainComplaint.getCrn());
        assertEquals("complaint description", domainComplaint.getDescription());
        final org.egov.pgrrest.common.model.Complainant domainComplainant = domainComplaint.getComplainant();
        assertNotNull(domainComplainant);
        assertEquals("firstName", domainComplainant.getFirstName());
        assertEquals("mobileNumber", domainComplainant.getMobile());
        assertEquals("email@email.com", domainComplainant.getEmail());
        assertEquals("address", domainComplainant.getAddress());
        final AuthenticatedUser authenticatedUser = domainComplaint.getAuthenticatedUser();
        assertNotNull(authenticatedUser);
        assertTrue(authenticatedUser.isAnonymousUser());
        final org.egov.pgrrest.read.domain.model.ComplaintType expectedComplaintType = new org.egov.pgrrest.read.domain.model.ComplaintType(
                "complaintName", "complaintCode", "tenantId");
        assertEquals(expectedComplaintType, domainComplaint.getComplaintType());
        assertEquals("EMAIL", domainComplaint.getReceivingMode());
        assertEquals("4", domainComplaint.getReceivingCenter());
        assertEquals("5", domainComplaint.getChildLocation());
        assertEquals("6", domainComplaint.getAssignee());
        assertEquals("7", domainComplaint.getState());
    }

    public Date toDate(LocalDateTime dateTime) {
        final ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneId.of(IST));
        return Date.from(zonedDateTime.toInstant());
    }
}