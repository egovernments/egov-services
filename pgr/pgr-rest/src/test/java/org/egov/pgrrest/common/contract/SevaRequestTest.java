package org.egov.pgrrest.common.contract;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pgrrest.common.model.AuthenticatedUser;
import org.egov.pgrrest.common.model.Requester;
import org.egov.pgrrest.common.model.UserType;
import org.egov.pgrrest.read.domain.model.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SevaRequestTest {

    private static final String CRN = "crn";
    private static final String TENANT_ID = "tenantId";

    @Test
    public void test_should_set_generated_crn_to_seva_request() {
        final org.egov.pgrrest.read.domain.model.ServiceRequest complaint = getComplaint();
        final SevaRequest sevaRequest = getSevaRequest();

        sevaRequest.update(complaint);

        assertEquals(CRN, sevaRequest.getServiceRequest().getCrn());
    }

    private org.egov.pgrrest.read.domain.model.ServiceRequest getComplaint() {
        final AuthenticatedUser user = getAuthenticatedUser();
        final Coordinates coordinates = new Coordinates(0d, 0d, TENANT_ID);
        final ServiceRequestLocation serviceRequestLocation = ServiceRequestLocation.builder().coordinates(coordinates)
                .crossHierarchyId("id").build();

        return org.egov.pgrrest.read.domain.model.ServiceRequest.builder().tenantId(TENANT_ID).authenticatedUser(user)
                .requester(Requester.builder().build()).crn(CRN).serviceRequestLocation(serviceRequestLocation)
                .complaintType(new ServiceRequestType(null, null, TENANT_ID)).build();
    }

    private AuthenticatedUser getAuthenticatedUser() {
        return AuthenticatedUser.builder()
                .id(1L)
                .type(UserType.CITIZEN)
                .build();
    }

    private SevaRequest getSevaRequest() {
        final RequestInfo requestInfo = new RequestInfo();
        final ServiceRequest serviceRequest = org.egov.pgrrest.common.contract.ServiceRequest.builder().build();
        return new SevaRequest(requestInfo, serviceRequest);
    }

}