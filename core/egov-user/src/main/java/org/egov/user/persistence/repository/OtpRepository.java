package org.egov.user.persistence.repository;

import org.egov.user.domain.model.User;
import org.egov.user.web.contract.Otp;
import org.egov.user.web.contract.OtpRequest;
import org.egov.user.web.contract.OtpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class OtpRepository {
    private final RestTemplate restTemplate;
    private final String otpSearchEndpoint;

    @Autowired
    public OtpRepository(@Value("${core.otp.service.url}") String otpServiceHost,
                         @Value("${egov.services.otp.search_otp}") String otpSearchContextEndpoint,
                         RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.otpSearchEndpoint = otpServiceHost + otpSearchContextEndpoint;
    }

    public boolean isOtpValidationComplete(User user) {
        Otp otp = Otp.builder().tenantId(user.getTenantId()).uuid(user.getOtpReference()).build();
        OtpRequest otpRequest = new OtpRequest(otp);
        OtpResponse otpResponse = restTemplate.postForObject(otpSearchEndpoint, otpRequest, OtpResponse.class);
        return otpResponse.isValidationComplete(user.getMobileNumber());
    }

}
