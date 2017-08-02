package org.egov.user.consumer;

import java.util.Map;

import org.egov.user.model.UserReq;
import org.egov.user.repository.NewUserRepository;
import org.egov.user.utils.UserConfigurationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserConsumer {
	
	@Autowired
	private NewUserRepository newUserRepository;
	
	@Autowired
	private UserConfigurationUtil userConfigurationUtil;
	
	@KafkaListener(topics = { "${kafka.topics.save.user}", "${kafka.topics.update.user}"})
	public void processMessage(Map<String, Object> consumerRecord, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		log.debug("key:" + topic + ":" + "value:" + consumerRecord);
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			
			if (userConfigurationUtil.getCreateUserTopic().equals(topic))
				newUserRepository.saveUser(objectMapper.convertValue(consumerRecord, UserReq.class));
			else if(userConfigurationUtil.getUpdateUserTopic().equals(topic))
				newUserRepository.updateUser(objectMapper.convertValue(consumerRecord, UserReq.class));
				
		} catch (Exception exception) {
			log.debug("UserConsumer processMessage:" + exception);
			throw exception;
		}
	}
	
}
