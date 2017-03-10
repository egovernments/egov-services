package org.egov.user.persistence.repository;

import org.egov.user.domain.model.User;
import org.egov.user.web.contract.Otp;
import org.egov.user.web.contract.OtpRequest;
import org.egov.user.web.contract.OtpResponse;
import org.egov.user.web.contract.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Repository
public class OtpRepository {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final RestTemplate restTemplate;
    private final String otpSearchEndpoint;

    @Autowired
    public OtpRepository(@Value("${core.otp.service.url}") String otpServiceHost,
                         @Value("${egov.services.otp.search_otp}") String otpSearchContextEndpoint,
                         RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.otpSearchEndpoint = otpServiceHost + otpSearchContextEndpoint;
    }

    public Boolean isOtpValidationComplete(RequestInfo requestInfo, User user) {
        try {
            Otp otp = Otp.builder().tenantId(user.getTenantId()).uuid(user.getOtpReference()).build();
            OtpRequest otpRequest = new OtpRequest(requestInfo, otp);
            OtpResponse otpResponse = restTemplate.postForObject(otpSearchEndpoint, otpRequest, OtpResponse.class);
            if (otpResponse == null || otpResponse.getOtp() == null) return Boolean.FALSE;
            Otp otpDetails = otpResponse.getOtp();
            return (otpDetails.getValidationSuccessful() && user.getMobileNumber().equalsIgnoreCase(otpDetails.getIdentity()));
        } catch (RestClientException rce) {
            logger.error(rce.getMessage(), rce);
            return Boolean.FALSE;
        }
    }

}
