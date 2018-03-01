package org.egov.user.notification.service;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.user.config.PropertiesManager;
import org.egov.user.domain.v11.model.Otp;
import org.egovuser.notification.web.contract.SmsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SMSService {

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	public void send(Otp request) {

		final SmsRequest smsRequest = SmsRequest.builder().message(getSMSMessage(request.getOtp()))
				.mobileNumber(request.getIdentity()).build();

		log.info("User Otp SMS details------------" + smsRequest);
		try {
			kafkaTemplate.send(propertiesManager.getSmsNotificationTopic(),
					propertiesManager.getSmsNotificationTopicKey(), smsRequest);
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}

	private String getSMSMessage(final String otp) {
		return "This is your otp " + otp + " Number ";
	}
}
