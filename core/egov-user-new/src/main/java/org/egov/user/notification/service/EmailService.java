package org.egov.user.notification.service;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.user.config.PropertiesManager;
import org.egov.user.domain.model.Otp;
import org.egov.user.notification.web.contract.EmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	public void send(Otp otpRequest) {
		final EmailRequest emailRequest = EmailRequest.builder()
				.subject(new StringBuilder().append("Otp number : ").toString())
				.body(getSmsMessage(otpRequest.getOtp())).build();

		log.info("User Otp Details------------" + emailRequest);
		try {
			kafkaTemplate.send(propertiesManager.getEmailNotificationTopic(),
					propertiesManager.getEmailNotificationTopicKey(), emailRequest);
		} catch (final Exception ex) {
			ex.printStackTrace();
		}

	}

	public String getSmsMessage(final String otp) {

		return "This is your otp " + otp + " Number ";
	}

}
