package org.egov.web.indexer.transform;

import org.egov.web.indexer.contract.SevaRequest;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class SevaRequestDeserializer extends JsonDeserializer<SevaRequest> {

	public SevaRequestDeserializer() {
		super(SevaRequest.class);
	}

}
