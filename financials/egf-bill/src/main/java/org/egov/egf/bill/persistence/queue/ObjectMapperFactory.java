package org.egov.egf.bill.persistence.queue;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperFactory {

	private final ObjectMapper objectMapper;

	public ObjectMapperFactory(final ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public ObjectMapper create() {
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		return objectMapper;
	}

}
