/*package org.egov.tl.masters.persistence.queue;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class DocumentTypeProducer {

	@Autowired
	KafkaTemplate<String, Object> mykafkaTemplate;

	@Autowired
	public DocumentTypeProducer(KafkaTemplate<String, Object> kafkaTemplate) {
		this.mykafkaTemplate = kafkaTemplate;
	}

	public void sendMessage(String topic, String key, Map<String, Object> message) {
		mykafkaTemplate.send(topic, key, message);
	}

}
*/