package org.egov.egf.persistence.repository;

import java.util.HashMap;

import org.egov.egf.persistence.queue.contract.SupplierContractRequest;
import org.egov.egf.producer.FinancialProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SupplierQueueRepository {

	@Autowired
	private FinancialProducer financialProducer;

	@Value("${kafka.topics.egf.masters.validated.topic}")
	private String supplierValidatedTopic;

	@Value("${kafka.topics.egf.masters.supplier.validated.key}")
	private String supplierValidatedKey;

	public void push(SupplierContractRequest supplierContractRequest) {
		HashMap<String, Object> supplierContractRequestMap = new HashMap<String, Object>();

		if ("create".equalsIgnoreCase(supplierContractRequest.getRequestInfo().getAction()))
			supplierContractRequestMap.put("SupplierCreate", supplierContractRequest);
		else if ("updateAll".equalsIgnoreCase(supplierContractRequest.getRequestInfo().getAction()))
			supplierContractRequestMap.put("SupplierUpdate", supplierContractRequest);
		else if ("update".equalsIgnoreCase(supplierContractRequest.getRequestInfo().getAction()))
			supplierContractRequestMap.put("SupplierUpdate", supplierContractRequest);

		financialProducer.sendMessage(supplierValidatedTopic, supplierValidatedKey, supplierContractRequestMap);
	}
}
