package org.egov.wf.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.wf.service.WfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WfMessageListener implements MessageListener<String, Object> {
	
	@Autowired
	private WfService wfService;
	
	@Override
	public void onMessage(ConsumerRecord<String, Object> data) {
        log.info("Topic: "+data.topic());
        log.info("Value: "+data.value());
        ObjectMapper objectMapper = new ObjectMapper();
		String rcvData = null;
		
		try {
			rcvData = objectMapper.writeValueAsString(data.value());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		wfService.makeWfCall(data.topic(), rcvData);
	}

}
