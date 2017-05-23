package org.egov.egf.persistence.repository;

import java.util.HashMap;

import org.egov.egf.persistence.queue.contract.SchemeContractRequest;
import org.egov.egf.producer.FinancialProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SchemeQueueRepository {

	@Autowired
	private FinancialProducer financialProducer;

	@Value("${kafka.topics.egf.masters.validated.topic}")
	private String schemeValidatedTopic;

	@Value("${kafka.topics.egf.masters.scheme.validated.key}")
	private String schemeValidatedKey;

	public void push(SchemeContractRequest schemeContractRequest) {
		HashMap<String, Object> schemeContractRequestMap = new HashMap<String, Object>();
		if (schemeContractRequest.getSchemes() != null && !schemeContractRequest.getSchemes().isEmpty())
			schemeContractRequestMap.put("SchemeCreate", schemeContractRequest);
		else if (schemeContractRequest.getScheme() != null
				&& schemeContractRequest.getScheme().getId() != null)
			schemeContractRequestMap.put("SchemeUpdate", schemeContractRequest);
		financialProducer.sendMessage(schemeValidatedKey, schemeValidatedKey, schemeContractRequestMap);
	}
}
