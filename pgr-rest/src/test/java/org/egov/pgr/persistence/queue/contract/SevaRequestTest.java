package org.egov.pgr.persistence.queue.contract;

import org.egov.pgr.domain.model.*;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class SevaRequestTest {

    private static final String CRN = "crn";
    private static final String TENANT_ID = "tenantId";

    @Test
    public void test_should_set_generated_crn_to_seva_request() {
        final Complaint complaint = getComplaint();
        final SevaRequest sevaRequest = getSevaRequest();

        sevaRequest.update(complaint);

        assertEquals(CRN, sevaRequest.getServiceRequest().getCrn());
    }

    @Test
    public void test_should_set_user_details_to_seva_request() {

        final Complaint complaint = getComplaint();
        final SevaRequest sevaRequest = getSevaRequest();

        sevaRequest.update(complaint);

        assertEquals("1", sevaRequest.getRequestInfo().getUserId());
        assertEquals(UserType.CITIZEN.toString(), sevaRequest.getRequestInfo().getUserType());
    }

    private Complaint getComplaint() {
        final AuthenticatedUser user = getAuthenticatedUser();
        return Complaint.builder()
                .jurisdictionId(TENANT_ID)
                .authenticatedUser(user)
                .complainant(Complainant.builder().build())
                .crn(CRN)
                .complaintLocation(new ComplaintLocation(new Coordinates(0d, 0d), "id"))
                .build();
    }

    private AuthenticatedUser getAuthenticatedUser() {
        return AuthenticatedUser.builder()
                .id(1)
                .type(Collections.singletonList(UserType.CITIZEN))
                .build();
    }

    private SevaRequest getSevaRequest() {
        final SevaRequest sevaRequest = new SevaRequest();
        sevaRequest.setRequestInfo(new RequestInfo());
        sevaRequest.setServiceRequest(ServiceRequest.builder().build());
        return sevaRequest;
    }

}