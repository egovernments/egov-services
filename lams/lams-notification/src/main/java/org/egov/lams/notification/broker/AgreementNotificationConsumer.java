package org.egov.lams.notification.broker;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.lams.notification.adapter.AgreementNotificationAdapter;
import org.egov.lams.notification.model.Agreement;
import org.egov.lams.notification.web.contract.AgreementRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AgreementNotificationConsumer {

	public static final Logger LOGGER = LoggerFactory.getLogger(AgreementNotificationConsumer.class);

	@Autowired
	private AgreementNotificationAdapter agreementNotificationAdapter;

	@KafkaListener(containerFactory = "kafkaListenerContainerFactory", topics = {
			"${kafka.topics.notification.agreement.create.name}", "${kafka.topics.notification.agreement.update.name}"})
	public void listen(ConsumerRecord<String, String> record) {
		LOGGER.info("key:" + record.key() + ":" + "value:" + record.value());

			ObjectMapper objectMapper = new ObjectMapper();
			try {
				agreementNotificationAdapter
						.sendSmsNotification(objectMapper.readValue(record.value(), AgreementRequest.class));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}