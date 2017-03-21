package org.egov.egf.persistence.repository;

import java.util.HashMap;

import org.egov.egf.persistence.queue.contract.BankContractRequest;
import org.egov.egf.producer.FinancialProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class BankQueueRepository {

	@Autowired
	private FinancialProducer financialProducer;

	private String queueNameSuffix;

	@Autowired
	public BankQueueRepository(KafkaTemplate<String, Object> kafkaTemplate,
			@Value("${kafka.topics.egf.master.validated.name}") String queueNameSuffix) {
		this.queueNameSuffix = queueNameSuffix;
	}

	public void push(BankContractRequest bankContractRequest) {
		String topicName = queueNameSuffix;
		HashMap<String, Object> bankContractRequestMap = new HashMap<String, Object>();
		bankContractRequestMap.put("Bank", bankContractRequest);
		financialProducer.sendMessage(topicName, bankContractRequestMap);
	}
}
