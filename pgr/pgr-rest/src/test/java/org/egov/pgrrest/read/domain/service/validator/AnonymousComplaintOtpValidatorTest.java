package org.egov.pgrrest.read.domain.service.validator;

import org.egov.pgrrest.common.domain.model.AuthenticatedUser;
import org.egov.pgrrest.common.domain.model.Requester;
import org.egov.pgrrest.read.domain.model.ServiceRequest;
import org.egov.pgrrest.read.domain.model.ServiceRequestLocation;
import org.egov.pgrrest.read.domain.model.ServiceRequestType;
import org.egov.pgrrest.read.domain.service.OtpService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AnonymousComplaintOtpValidatorTest {

    @InjectMocks
    private AnonymousComplaintOtpValidator validator;

    @Mock
    private OtpService otpService;

    @Test
    public void test_should_validate_anonymous_complaints() {
        final ServiceRequestType serviceRequestType = mock(ServiceRequestType.class);
        when(serviceRequestType.isComplaintType()).thenReturn(true);
        final AuthenticatedUser user = mock(AuthenticatedUser.class);
        when(user.isAnonymousUser()).thenReturn(true);
        final ServiceRequest serviceRequest = createServiceRequest(serviceRequestType, user);

        assertTrue(validator.canValidate(serviceRequest));
    }

    @Test
    public void test_should_not_validate_non_anonymous_complaints() {
        final ServiceRequestType serviceRequestType = mock(ServiceRequestType.class);
        when(serviceRequestType.isComplaintType()).thenReturn(true);
        final AuthenticatedUser user = mock(AuthenticatedUser.class);
        when(user.isAnonymousUser()).thenReturn(false);
        final ServiceRequest serviceRequest = createServiceRequest(serviceRequestType, user);

        assertFalse(validator.canValidate(serviceRequest));
    }

    @Test
    public void test_should_not_validate_non_complaints() {
        final ServiceRequestType serviceRequestType = mock(ServiceRequestType.class);
        when(serviceRequestType.isComplaintType()).thenReturn(false);
        final AuthenticatedUser user = mock(AuthenticatedUser.class);
        when(user.isAnonymousUser()).thenReturn(false);
        final ServiceRequest serviceRequest = createServiceRequest(serviceRequestType, user);

        assertFalse(validator.canValidate(serviceRequest));
    }

    @Test
    public void test_should_invoke_otp_service_to_validate_otp() {
        final ServiceRequestType serviceRequestType = mock(ServiceRequestType.class);
        when(serviceRequestType.isComplaintType()).thenReturn(false);
        final AuthenticatedUser user = mock(AuthenticatedUser.class);
        when(user.isAnonymousUser()).thenReturn(false);
        final ServiceRequest serviceRequest = createServiceRequest(serviceRequestType, user);

        validator.validate(serviceRequest);

        verify(otpService).validateOtp(serviceRequest);
    }

    private ServiceRequest createServiceRequest(ServiceRequestType serviceRequestType, AuthenticatedUser user) {
        final ServiceRequestLocation serviceRequestLocation = ServiceRequestLocation.builder()
            .build();
        final Requester requester = Requester.builder()
            .build();
        return ServiceRequest.builder()
            .attributeEntries(null)
            .authenticatedUser(user)
            .serviceRequestLocation(serviceRequestLocation)
            .requester(requester)
            .serviceRequestType(serviceRequestType)
            .tenantId("tenantId")
            .build();
    }
    
}