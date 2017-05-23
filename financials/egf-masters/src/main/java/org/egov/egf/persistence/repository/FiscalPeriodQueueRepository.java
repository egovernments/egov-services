package org.egov.egf.persistence.repository;

import java.util.HashMap;

import org.egov.egf.persistence.queue.contract.FiscalPeriodContractRequest;
import org.egov.egf.producer.FinancialProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FiscalPeriodQueueRepository {

	@Autowired
	private FinancialProducer financialProducer;

	@Value("${kafka.topics.egf.masters.validated.topic}")
	private String fiscalPeriodValidatedTopic;

	@Value("${kafka.topics.egf.masters.fiscalperiod.validated.key}")
	private String fiscalPeriodValidatedKey;
	
	public void push(FiscalPeriodContractRequest fiscalPeriodContractRequest) {
		HashMap<String, Object> fiscalPeriodContractRequestMap = new HashMap<String, Object>();
		if (fiscalPeriodContractRequest.getFiscalPeriods() != null
				&& !fiscalPeriodContractRequest.getFiscalPeriods().isEmpty())
			fiscalPeriodContractRequestMap.put("FiscalPeriodCreate", fiscalPeriodContractRequest);
		else if (fiscalPeriodContractRequest.getFiscalPeriod() != null
				&& fiscalPeriodContractRequest.getFiscalPeriod().getId() != null)
			fiscalPeriodContractRequestMap.put("FiscalPeriodUpdate", fiscalPeriodContractRequest);
		financialProducer.sendMessage(fiscalPeriodValidatedKey, fiscalPeriodValidatedKey,
				fiscalPeriodContractRequestMap);
	}
}
