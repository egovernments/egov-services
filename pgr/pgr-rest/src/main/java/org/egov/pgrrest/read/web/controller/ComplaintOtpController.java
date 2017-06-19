package org.egov.pgrrest.read.web.controller;

import org.egov.pgrrest.read.domain.service.OtpService;
import org.egov.pgrrest.read.web.contract.OtpRequest;
import org.egov.pgrrest.read.web.contract.OtpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class ComplaintOtpController {

    private OtpService otpService;

    public ComplaintOtpController(OtpService otpService) {
        this.otpService = otpService;
    }

    @PostMapping("/v1/otp/_send")
    @ResponseStatus(HttpStatus.CREATED)
    public OtpResponse sendOtp(@RequestBody OtpRequest otpRequest) {
        otpService.sendOtp(otpRequest.toDomain());
        return new OtpResponse(null, true);
    }
}
