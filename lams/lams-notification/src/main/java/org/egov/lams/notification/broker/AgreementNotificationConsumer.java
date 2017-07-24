package org.egov.lams.notification.broker;

import java.util.Map;

import org.egov.lams.notification.adapter.AgreementNotificationAdapter;
import org.egov.lams.notification.web.contract.AgreementRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AgreementNotificationConsumer {

	public static final Logger LOGGER = LoggerFactory.getLogger(AgreementNotificationConsumer.class);

	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	private AgreementNotificationAdapter agreementNotificationAdapter;

	@KafkaListener(topics = {"${kafka.topics.notification.agreement.create.name}", "${kafka.topics.notification.agreement.update.name}"})
	public void processMessage(Map<String, Object> consumerRecord, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		log.debug("key:" + topic + ":" + "value:" + consumerRecord);

			try {
				agreementNotificationAdapter
						.sendNotification(objectMapper.convertValue(consumerRecord, AgreementRequest.class));
			}catch (Exception exception) {
				log.debug("processMessage:" + exception);
				throw exception;
			}
	}
}