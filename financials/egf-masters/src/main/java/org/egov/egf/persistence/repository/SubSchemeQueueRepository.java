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

	public void push(SubSchemeContractRequest SubSchemeContractRequest) {
		HashMap<String, Object> SubSchemeContractRequestMap = new HashMap<String, Object>();
		if (SubSchemeContractRequest.getSubSchemes() != null && !SubSchemeContractRequest.getSubSchemes().isEmpty())
			SubSchemeContractRequestMap.put("SubSchemeCreate", SubSchemeContractRequest);
		else if (SubSchemeContractRequest.getSubScheme() != null
				&& SubSchemeContractRequest.getSubScheme().getId() != null)
			SubSchemeContractRequestMap.put("SubSchemeUpdate", SubSchemeContractRequest);
		financialProducer.sendMessage(SubSchemeValidatedTopic, SubSchemeValidatedKey, SubSchemeContractRequestMap);
	}
}
