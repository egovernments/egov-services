package org.egov.pgrrest.read.domain.service.validator;

import org.egov.pgrrest.common.domain.model.AuthenticatedUser;
import org.egov.pgrrest.common.domain.model.Requester;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.egov.pgrrest.read.domain.model.ServiceRequestLocation;
import org.egov.pgrrest.read.domain.model.ServiceRequestType;
import org.egov.pgrrest.read.domain.service.UpdateServiceRequestEligibilityService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UpdateRequestEligibilityValidatorTest {

    @InjectMocks
    private UpdateRequestEligibilityValidator validator;

    @Mock
    private UpdateServiceRequestEligibilityService eligibilityService;

    @Test
    public void test_can_validation_should_return_true_for_update_request() {
        final ServiceRequest serviceRequest = createServiceRequest(true);

         assertTrue(validator.canValidate(serviceRequest));
    }

    @Test
    public void test_can_validation_should_return_false_for_create_request() {
        final ServiceRequest serviceRequest = createServiceRequest(false);

        assertFalse(validator.canValidate(serviceRequest));
    }

    @Test
    public void test_should_perform_update_eligibility_validation() {
        final ServiceRequest serviceRequest = createServiceRequest(true);

        validator.validate(serviceRequest);

        verify(eligibilityService).validate(eq("crn"), eq("tenant"), any(AuthenticatedUser.class));
    }

    private ServiceRequest createServiceRequest(boolean isUpdate) {
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
            .modifyServiceRequest(isUpdate)
            .crn("crn")
            .tenantId("tenant")
            .build();
    }

}