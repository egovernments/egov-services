package org.egov.wf.consumer;

import org.egov.wf.service.WfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WfConsumer {
	
	@Autowired
	private WfService wfService;
	
	/*@KafkaListener(topics = {"start-land-wf","update-land-wf"})
	public void processMessage(Map<String, Object> consumerRecord, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		log.debug("key:" + topic + ":" + "value:" + consumerRecord);
		
		ObjectMapper objectMapper = new ObjectMapper();
		String body = null;
		try {
			body = objectMapper.writeValueAsString(consumerRecord);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		wfService.makeWfCall(topic, body);
	}*/

}
