package org.egov.egf.master.persistence.queue;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MastersQueueRepository {

	@Autowired
	private FinancialProducer financialProducer;

	@Value("${kafka.topics.egf.masters.validated.topic}")
	private String validatedTopic;

	@Value("${kafka.topics.egf.masters.validated.key}")
	private String validatedKey;

	public void add(Map<String, Object> topicMap) {
		financialProducer.sendMessage(validatedTopic, validatedKey, topicMap);
	}
}
