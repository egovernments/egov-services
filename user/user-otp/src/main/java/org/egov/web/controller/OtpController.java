package org.egov.web.controller;

import org.egov.domain.service.OtpService;
import org.egov.web.contract.OtpRequest;
import org.egov.web.contract.OtpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OtpController {

    private OtpService otpService;

    @Autowired
    public OtpController(OtpService otpService) {
        this.otpService = otpService;
    }

    public OtpResponse sendOtp(@RequestBody OtpRequest otpRequest) {
        otpService.sendOtp(otpRequest.toDomain());
        return null;
    }

}
