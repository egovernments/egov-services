package org.egov.egf.bill.persistence.queue.repository;

import java.util.HashMap;

import org.egov.egf.bill.persistence.queue.FinancialBillRegisterProducer;
import org.egov.egf.bill.web.requests.BillRegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BillRegisterQueueRepository {

	private FinancialBillRegisterProducer financialBillRegisterProducer;

	private String validatedTopic;

	private String instrumentValidatedKey;

	private String completedTopic;

	private String instrumentCompletedKey;

	@Autowired
	public BillRegisterQueueRepository(FinancialBillRegisterProducer financialBillRegisterProducer,
			@Value("${kafka.topics.egf.bill.validated.topic}") String validatedTopic,
			@Value("${kafka.topics.egf.bill.instrument.validated.key}") String instrumentValidatedKey,
			@Value("${kafka.topics.egf.bill.completed.topic}") String completedTopic,
			@Value("${kafka.topics.egf.bill.instrument.completed.key}") String instrumentCompletedKey) {

		this.financialBillRegisterProducer = financialBillRegisterProducer;
		this.validatedTopic = validatedTopic;
		this.instrumentValidatedKey = instrumentValidatedKey;
		this.completedTopic = completedTopic;
		this.instrumentCompletedKey = instrumentCompletedKey;
	}

	public void addToQue(BillRegisterRequest request) {
		HashMap<String, Object> topicMap = new HashMap<String, Object>();

		switch (request.getRequestInfo().getAction().toLowerCase()) {

		case "create":
			topicMap.put("billregister_create", request);
			System.out.println("push create topic" + request);
			break;
		case "update":
			topicMap.put("billregister_update", request);
			break;
		case "delete":
			topicMap.put("billregister_delete", request);
			break;

		}
		financialBillRegisterProducer.sendMessage(validatedTopic, instrumentValidatedKey, topicMap);
	}

	public void addToSearchQue(BillRegisterRequest request) {

		HashMap<String, Object> topicMap = new HashMap<String, Object>();

		if (!request.getBillRegisters().isEmpty()) {

			topicMap.put("billregister_persisted", request);

			System.out.println("push search topic" + request);

		}

		financialBillRegisterProducer.sendMessage(completedTopic, instrumentCompletedKey, topicMap);

	}
}
