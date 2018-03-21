package org.egov.user.persistence.repository;

import org.egov.user.domain.model.OtpValidationRequest;
import org.egov.user.persistence.dto.Otp;
import org.egov.user.persistence.dto.OtpRequest;
import org.egov.user.persistence.dto.OtpResponse;
import org.egov.user.web.contract.OtpValidateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Repository
public class OtpRepository {
    private final RestTemplate restTemplate;
    private final String otpSearchEndpoint;
    private final String otpValidateEndpoint;

    @Autowired
    public OtpRepository(@Value("${core.otp.service.url}") String otpServiceHost,
                         @Value("${egov.services.otp.search_otp}") String otpSearchContextEndpoint,
                         @Value("${egov.services.otp.validate_otp}") String otpValidateEndpoint,
                         RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.otpSearchEndpoint = otpServiceHost + otpSearchContextEndpoint;
        this.otpValidateEndpoint = otpServiceHost + otpValidateEndpoint;
    }

	public boolean isOtpValidationComplete(OtpValidationRequest request) {
		Otp otp = Otp.builder().tenantId(request.getTenantId()).uuid(request.getOtpReference()).build();
		OtpRequest otpRequest = new OtpRequest(otp);
		OtpResponse otpResponse = restTemplate.postForObject(otpSearchEndpoint, otpRequest, OtpResponse.class);
		return otpResponse.isValidationComplete(request.getMobileNumber());
	}

	public boolean validateOtp(OtpValidateRequest request) throws Exception{
		// TODO Auto-generated method stub
		OtpResponse otpResponse = null;
		try {
		 otpResponse = restTemplate.postForObject(otpValidateEndpoint, request, OtpResponse.class);
		}catch(HttpClientErrorException e){
			System.out.println(" the body : "+ e.getResponseBodyAsString());
			throw new Exception(e.getResponseBodyAsString());
		}
		if(null!=otpResponse && null!=otpResponse.getOtp())
		return otpResponse.getOtp().isValidationSuccessful();
		return false;
	}
}

