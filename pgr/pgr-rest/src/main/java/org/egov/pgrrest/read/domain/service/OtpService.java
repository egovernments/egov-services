package org.egov.pgrrest.read.domain.service;

import org.egov.pgr.common.model.OtpRequest;
import org.egov.pgr.common.repository.OtpRepository;
import org.egov.pgr.common.repository.OtpSMSRepository;
import org.springframework.stereotype.Service;

@Service
public class OtpService {

    private OtpRepository otpRepository;
    private OtpSMSRepository otpSMSSender;

    public OtpService(OtpRepository otpRepository, OtpSMSRepository otpSMSSender) {
        this.otpRepository = otpRepository;
        this.otpSMSSender = otpSMSSender;
	}

    public void sendOtp(OtpRequest otpRequest) {
        otpRequest.validate();
        final String otpNumber = otpRepository.fetchOtp(otpRequest);
        otpSMSSender.send(otpRequest, otpNumber);
    }

}

