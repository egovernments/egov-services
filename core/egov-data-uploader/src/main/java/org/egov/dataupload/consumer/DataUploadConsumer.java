package org.egov.dataupload.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;

import org.egov.dataupload.model.ConsumerRequest;
import org.egov.dataupload.service.DataUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class DataUploadConsumer {
	
	public static final Logger logger = LoggerFactory.getLogger(DataUploadConsumer.class);
	    
    @Value("${kafka.topics.dataupload}")
    private String topic;
    
	@Autowired
	private DataUploadService dataUploadService;
	
	@KafkaListener(topics = {"${kafka.topics.dataupload}"})
	
	public void listen(final HashMap<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		ObjectMapper mapper = new ObjectMapper();
		logger.info("Record: " + record.toString());
		try{
			logger.info("Consuming record: "+record);
			dataUploadService.doInterServiceCall(mapper.convertValue(record, ConsumerRequest.class));
		}catch(final Exception e){
			logger.error("Error while listening to value: "+record+" on topic: "+topic+": ", e.getMessage());
		}
	}

}
