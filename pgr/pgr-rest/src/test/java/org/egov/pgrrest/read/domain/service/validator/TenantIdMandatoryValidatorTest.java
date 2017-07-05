package org.egov.pgrrest.read.domain.service.validator;

import org.egov.pgrrest.common.domain.model.AuthenticatedUser;
import org.egov.pgrrest.common.domain.model.Requester;
import org.egov.pgrrest.read.domain.exception.TenantIdMandatoryException;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.egov.pgrrest.read.domain.model.ServiceRequestLocation;
import org.egov.pgrrest.read.domain.model.ServiceRequestType;
import org.junit.Test;

public class TenantIdMandatoryValidatorTest {

    @Test(expected = TenantIdMandatoryException.class)
    public void test_should_throw_exception_when_tenant_id_is_not_present() {
        final ServiceRequest serviceRequest = createServiceRequest(null);
        final TenantIdMandatoryValidator validator = new TenantIdMandatoryValidator();

        validator.validate(serviceRequest);
    }

    @Test
    public void test_should_not_throw_exception_when_tenant_id_is_present() {
        final ServiceRequest serviceRequest = createServiceRequest("tenantId");
        final TenantIdMandatoryValidator validator = new TenantIdMandatoryValidator();

        validator.validate(serviceRequest);
    }

    private ServiceRequest createServiceRequest(String tenantId) {
        final ServiceRequestLocation serviceRequestLocation = ServiceRequestLocation.builder()
            .build();
        final Requester requester = Requester.builder()
            .build();
        final ServiceRequestType serviceRequestType = ServiceRequestType.builder()
            .build();
        return ServiceRequest.builder()
            .attributeEntries(null)
            .authenticatedUser(AuthenticatedUser.createAnonymousUser())
            .serviceRequestLocation(serviceRequestLocation)
            .requester(requester)
            .serviceRequestType(serviceRequestType)
            .tenantId(tenantId)
            .build();
    }

}