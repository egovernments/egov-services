package org.egov.domain.service;

import org.egov.domain.model.OtpRequest;
import org.egov.persistence.repository.OtpMessageRepository;
import org.egov.persistence.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OtpService {

    private OtpRepository otpRepository;
    private OtpMessageRepository otpSender;

    @Autowired
    public OtpService(OtpRepository otpRepository,
                      OtpMessageRepository otpSender) {
        this.otpRepository = otpRepository;
        this.otpSender = otpSender;
    }

    public void sendOtp(OtpRequest otpRequest) {
        otpRequest.validate();
        final String otpNumber = otpRepository.fetchOtp(otpRequest);
        otpSender.send(otpRequest, otpNumber);
    }

}

