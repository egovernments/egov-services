package org.egov.egf.persistence.repository;

import java.util.HashMap;

import org.egov.egf.persistence.queue.contract.SubSchemeContractRequest;
import org.egov.egf.producer.FinancialProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SubSchemeQueueRepository {

	@Autowired
	private FinancialProducer financialProducer;

	@Value("${kafka.topics.egf.masters.validated.topic}")
	private String SubSchemeValidatedTopic;

	@Value("${kafka.topics.egf.masters.SubScheme.validated.key}")
	private String SubSchemeValidatedKey;

	public void push(SubSchemeContractRequest subSchemeContractRequest) {
		HashMap<String, Object> subSchemeContractRequestMap = new HashMap<String, Object>();
		if ("create".equalsIgnoreCase(subSchemeContractRequest.getRequestInfo().getAction()))
			subSchemeContractRequestMap.put("SubSchemeCreate", subSchemeContractRequest);
		else if ("updateAll".equalsIgnoreCase(subSchemeContractRequest.getRequestInfo().getAction()))
			subSchemeContractRequestMap.put("SubSchemeUpdateAll", subSchemeContractRequest);
		else if ("update".equalsIgnoreCase(subSchemeContractRequest.getRequestInfo().getAction()))
			subSchemeContractRequestMap.put("SubSchemeUpdate", subSchemeContractRequest);

		financialProducer.sendMessage(SubSchemeValidatedTopic, SubSchemeValidatedKey, subSchemeContractRequestMap);
	}
}
