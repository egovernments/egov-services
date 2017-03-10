package org.egov.user.domain.service;

import org.egov.user.domain.model.User;
import org.egov.user.persistence.repository.OtpRepository;
import org.egov.user.web.contract.RequestInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OtpServiceTest {

    @Mock
    OtpRepository otpRepository;
    @Mock
    RequestInfo requestInfo;
    @Mock
    User user;
    @InjectMocks
    OtpService otpService;

    @Test
    public void testShouldDelegateOtpValidationToRepository() throws Exception {
        otpService.isOtpValidationComplete(requestInfo, user);

        verify(otpRepository, times(1)).isOtpValidationComplete(requestInfo, user);
    }
}