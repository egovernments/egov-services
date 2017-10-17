package org.egov.propertyUser.userConsumer;

import java.util.Map;

import org.egov.models.TitleTransfer;
import org.egov.models.TitleTransferRequest;
import org.egov.models.User;
import org.egov.propertyUser.config.PropertiesManager;
import org.egov.propertyUser.util.UserUtil;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@EnableKafka
@Slf4j
public class TitleTransferConsumer {
	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
	
	@Autowired
	private UserUtil userUtil;

	/**
	 * This method will listen property object from producer and check user
	 * authentication Updating auth token in UserAuthResponseInfo Search user
	 * Create user
	 */
	@KafkaListener(topics = { "#{propertiesManager.getCreateTitletransferValidator()}",
			"#{propertiesManager.getUpdateTitletransferValidator()}" })
	public void receive(Map<String, Object> consumerRecord, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic)  throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		TitleTransferRequest titleTransferRequest = objectMapper.convertValue(consumerRecord,
				TitleTransferRequest.class);
		log.info("consumer topic value is: " + topic + " consumer value is" + titleTransferRequest);
		TitleTransfer titleTransfer = titleTransferRequest.getTitleTransfer();
		for (User user : titleTransfer.getNewOwners()) {
			user.setUserName(user.getMobileNumber());

			user = userUtil.getUserId(user, titleTransferRequest.getRequestInfo());

		}

		if (topic.equalsIgnoreCase(propertiesManager.getCreateTitletransferValidator()))
			kafkaTemplate.send(propertiesManager.getCreateTitletransferUserValidator(), titleTransferRequest);

		else if (topic.equalsIgnoreCase(propertiesManager.getUpdateTitletransferValidator()))
			kafkaTemplate.send(propertiesManager.getUpdateTitletransferUserValidator(), titleTransferRequest);
	}

}
