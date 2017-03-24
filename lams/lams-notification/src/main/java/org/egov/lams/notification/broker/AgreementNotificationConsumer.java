package org.egov.lams.notification.broker;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.lams.notification.adapter.AgreementNotificationAdapter;
import org.egov.lams.notification.models.Agreement;
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
			"${kafka.topics.notification.agreement.name}", "${kafka.topics.notification.approval.name}",
			"${kafka.topics.notification.reject.name}" })
	public void listen(ConsumerRecord<String, String> record) {
		LOGGER.info("key:" + record.key() + ":" + "value:" + record.value());

		if (record.topic().equals("agreement-save-db")) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				agreementNotificationAdapter
						.sendSmsNotification(objectMapper.readValue(record.value(), Agreement.class));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (record.topic().equals("agreement-workflow")) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				agreementNotificationAdapter
						.sendApprovalNotification(objectMapper.readValue(record.value(), Agreement.class));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		else if (record.topic().equals("agreement-rejected")) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				agreementNotificationAdapter
						.sendRejectedNotification(objectMapper.readValue(record.value(), Agreement.class));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}