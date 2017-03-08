package org.egov.web.controller;

import org.egov.web.contract.OtpRequest;
import org.egov.web.contract.OtpResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OtpController {

    public OtpResponse sendOtp(@RequestBody OtpRequest otpRequest) {
        return null;
    }

}
