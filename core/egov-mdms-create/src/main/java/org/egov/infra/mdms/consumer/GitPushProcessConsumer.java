package org.egov.infra.mdms.consumer;


import java.util.HashMap;

import org.egov.infra.mdms.service.MDMSService;
import org.egov.infra.mdms.utils.GitPushProcessWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GitPushProcessConsumer {
	
	public static final Logger logger = LoggerFactory.getLogger(GitPushProcessConsumer.class);
	        
	@Autowired
	private MDMSService mdmsService;
	
	@KafkaListener(topics = {"${egov.kafka.topics.gitpushprocess}"})
	
	public void listen(final HashMap<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		ObjectMapper mapper = new ObjectMapper();
		try{
			logger.info("Consuming record: "+record);
			mdmsService.gitPushProcess(mapper.convertValue(record, GitPushProcessWrapper.class));
		}catch(final Exception e){
			logger.error("Error while listening to value: "+record+" on topic: "+topic+": ", e.getMessage());
		}
	}

}
