package org.egov.pgrrest.read.web.controller;

import org.egov.pgrrest.read.domain.service.OtpService;
import org.egov.pgrrest.read.web.contract.OtpRequest;
import org.egov.pgrrest.read.web.contract.OtpResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ComplaintOtpController {

    private OtpService otpService;

    public ComplaintOtpController(OtpService otpService) {
        this.otpService = otpService;
    }

    @PostMapping("/v1/otp/_send")
    public OtpResponse sendOtp(@RequestBody OtpRequest otpRequest) {
        otpService.sendOtp(otpRequest.toDomain());
        return new OtpResponse(null, true);
    }
}
