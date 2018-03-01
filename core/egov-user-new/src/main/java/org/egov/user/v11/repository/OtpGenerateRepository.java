package org.egov.user.v11.repository;


import org.egov.user.domain.v11.model.OtpRequest;
import org.egov.user.web.v11.contract.OtpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class OtpGenerateRepository {

    private final String otpCreateUrl;
    private RestTemplate restTemplate;

    public OtpGenerateRepository(RestTemplate restTemplate,
                         @Value("${otp.host}") String otpHost,
                         @Value("${otp.create.url}") String otpCreateUrl) {
        this.restTemplate = restTemplate;
        this.otpCreateUrl = otpHost + otpCreateUrl;
    }

    public String fetchOtp(OtpRequest otpRequest) {
        final org.egov.user.web.v11.contract.OtpRequest request =
                new org.egov.user.web.v11.contract.OtpRequest(otpRequest);
        final OtpResponse otpResponse =
                restTemplate.postForObject(otpCreateUrl, request, OtpResponse.class);
        return otpResponse.getOtp().getOtp();
    }
}
