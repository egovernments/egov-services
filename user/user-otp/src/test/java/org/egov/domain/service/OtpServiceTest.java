package org.egov.domain.service;

import org.egov.domain.model.OtpRequest;
import org.egov.persistence.repository.OtpMessageRepository;
import org.egov.persistence.repository.OtpRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OtpServiceTest {
    @Mock
    private OtpRepository otpRepository;

    @Mock
    private OtpMessageRepository otpMessageRepository;

    @InjectMocks
    private OtpService otpService;

    @Test
    public void test_should_validate_otp_request() {
        final OtpRequest otpRequest = mock(OtpRequest.class);

        otpService.sendOtp(otpRequest);

        verify(otpRequest).validate();
    }

    @Test
    public void test_should_send_otp() {
        final OtpRequest otpRequest = mock(OtpRequest.class);
        final String otpNumber = "otpNumber";
        when(otpRepository.fetchOtp(otpRequest)).thenReturn(otpNumber);

        otpService.sendOtp(otpRequest);

        verify(otpMessageRepository).send(otpRequest, otpNumber);
    }
}