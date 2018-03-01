package org.egov.user.notification.consumer;

import java.util.HashMap;

import org.egov.user.domain.v11.model.Otp;
import org.egov.user.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserNotificationListener {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private NotificationService notificationService;

	@KafkaListener(topics = "${kafka.topics.notification.user.otp}")
	public void process(HashMap<String, Object> userSmsRequestMap) {
		Otp otpRequest = objectMapper.convertValue(userSmsRequestMap, Otp.class);
		notificationService.notify(otpRequest);
	}

}