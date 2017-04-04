package org.egov.egf.master.index.persistence.queue.contract;

import org.springframework.kafka.support.serializer.JsonDeserializer;

public class BankRequestDeserializer extends JsonDeserializer<BankContractRequest> {

	public BankRequestDeserializer() {
		super(BankContractRequest.class);
	}
}
