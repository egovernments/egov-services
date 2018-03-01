package org.egov.user.persistence.repository;

import org.egov.user.domain.model.OtpValidationRequest;
import org.egov.user.persistence.dto.Otp;
import org.egov.user.persistence.dto.OtpRequest;
import org.egov.user.persistence.dto.OtpResponse;
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

	public boolean isOtpValidationComplete(OtpValidationRequest request) {
		Otp otp = Otp.builder().tenantId(request.getTenantId()).uuid(request.getOtpReference()).build();
		OtpRequest otpRequest = new OtpRequest(otp);
		OtpResponse otpResponse = restTemplate.postForObject(otpSearchEndpoint, otpRequest, OtpResponse.class);
		return otpResponse.isValidationComplete(request.getMobileNumber());
	}
}
