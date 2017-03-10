package org.egov.egf.persistence.queue;

import org.egov.egf.persistence.queue.contract.BankContractRequest;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class FinancialGenericDeserializer extends JsonDeserializer<BankContractRequest> {

	public FinancialGenericDeserializer() {
		super(BankContractRequest.class);
	}
}
