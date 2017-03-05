package org.egov.web.controller;

import org.egov.domain.model.Token;
import org.egov.domain.service.TokenService;
import org.egov.web.contract.OtpRequest;
import org.egov.web.contract.OtpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OtpController {

    private TokenService tokenService;

    public OtpController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/_create")
    @ResponseStatus(HttpStatus.CREATED)
    public OtpResponse createOtp(@RequestBody OtpRequest otpRequest) {
        final Token token = tokenService.createToken(otpRequest.getTokenRequest());
        return new OtpResponse(token);
    }
}

