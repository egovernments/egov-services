package org.egov.pgr.common.repository;

import org.egov.pgr.common.model.OtpRequest;
import org.egov.pgr.common.model.OtpValidationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OtpRepository {

    private static final String OTP_CREATE = "otp/v1/_create";
    private static final String OTP_SEARCH = "otp/v1/_search";
    private final String otpCreateUrl;
    private final String otpSearchUrl;
    private RestTemplate restTemplate;

    public OtpRepository(RestTemplate restTemplate,
                         @Value("${otp.host}") String otpHost) {
        this.restTemplate = restTemplate;
        this.otpCreateUrl = otpHost + OTP_CREATE;
        this.otpSearchUrl = otpHost + OTP_SEARCH;
    }

    public String fetchOtp(OtpRequest otpRequest) {
        final org.egov.pgr.common.repository.OtpRequest request =
                new org.egov.pgr.common.repository.OtpRequest(otpRequest);
        final OtpResponse otpResponse =
                restTemplate.postForObject(otpCreateUrl, request, OtpResponse.class);

        return otpResponse.getOtpNumber();
    }

    public boolean isOtpValidationComplete(OtpValidationRequest request) {
        Otp otp = Otp.builder().tenantId(request.getTenantId()).uuid(request.getOtpReference()).build();
        org.egov.pgr.common.repository.OtpRequest otpRequest = new org.egov.pgr.common.repository.OtpRequest(otp);
        OtpResponse otpResponse = restTemplate.postForObject(otpSearchUrl, otpRequest, OtpResponse.class);
        return otpResponse.isValidationComplete(request.getMobileNumber());
    }

}