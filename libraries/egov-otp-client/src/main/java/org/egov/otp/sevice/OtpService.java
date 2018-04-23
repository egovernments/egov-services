package org.egov.otp.sevice;

import java.util.HashMap;
import java.util.Map;

import org.egov.otp.model.OtpRequest;
import org.egov.otp.model.OtpResponse;
import org.egov.tracer.model.CustomException;
import org.egov.tracer.model.ServiceCallException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OtpService {

	private RestTemplate restTemplate;

	@Value("${egov.otp.host:http://localhost:8080/}")
	private String otpHost;

	@Value("${egov.otp.host:otp/v1/_create}")
	private String sendOtpEndPoint;

	public OtpResponse sendOtp(OtpRequest otpRequest) {
		OtpResponse otpResponse = null;
		try {
			  otpResponse = restTemplate.postForObject(otpHost.concat(sendOtpEndPoint), otpRequest, OtpResponse.class);
		} catch (HttpClientErrorException ex) {
			ex.printStackTrace();
			String excep = ex.getResponseBodyAsString();
			log.info("HttpClientErrorException:" + excep);
			throw new ServiceCallException(excep);
		} catch (Exception ex) {
			Map<String, String> map = new HashMap<>();
			map.put(ex.getCause().getClass().getName(),ex.getMessage());
			throw new CustomException(map);
		}
		return otpResponse;
	}
}
