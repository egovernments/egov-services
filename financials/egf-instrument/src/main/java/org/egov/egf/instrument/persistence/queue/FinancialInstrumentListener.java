package org.egov.egf.instrument.persistence.queue;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FinancialInstrumentListener {

	@Value("${kafka.topics.egf.instrument.completed.topic}")
	private String completedTopic;

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private FinancialInstrumentProducer financialProducer;

	@KafkaListener(id = "${kafka.topics.egf.instrument.validated.id}", topics = "${kafka.topics.egf.instrument.validated.topic}", group = "${kafka.topics.egf.instrument.validated.group}")
	public void process(HashMap<String, Object> mastersMap) {

	}

}
