package org.egov.egf.instrument.persistence.queue;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class InstrumentQueueRepository {

	@Autowired
	private FinancialInstrumentProducer financialProducer;

	@Value("${kafka.topics.egf.instrument.validated.topic}")
	private String validatedTopic;

	@Value("${kafka.topics.egf.instrument.validated.key}")
	private String validatedKey;

	public void add(Map<String, Object> topicMap) {
		financialProducer.sendMessage(validatedTopic, validatedKey, topicMap);
	}
	
}
