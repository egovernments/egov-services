package org.egov.web.controller;

import org.egov.web.contract.OtpRequest;
import org.egov.web.contract.OtpResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OtpController {

    @PostMapping("/_create")
    public OtpResponse createOtp() {
        return null;
    }
}