package org.egov.egf.persistence.queue;

import java.util.HashMap;

import org.springframework.kafka.support.serializer.JsonDeserializer;

public class FinancialGenericDeserializer extends JsonDeserializer<HashMap> {

	public FinancialGenericDeserializer() {
		super(HashMap.class);
	}
}
