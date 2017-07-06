package org.egov.egf.instrument.persistence.queue;

import java.util.HashMap;

import org.egov.common.web.contract.CommonRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FinancialInstrumentListener {

	@Value("${kafka.topics.egf.masters.completed.topic}")
	private String completedTopic;

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private FinancialProducer financialProducer;

	@KafkaListener(id = "${kafka.topics.egf.masters.validated.id}", topics = "${kafka.topics.egf.masters.validated.topic}", group = "${kafka.topics.egf.masters.validated.group}")
	public void process(HashMap<String, CommonRequest<?>> mastersMap) {

	}

}
