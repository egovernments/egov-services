package org.egov.web.indexer.transform;

import java.util.HashMap;

import org.egov.web.indexer.models.ComplaintIndex;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class ComplaintIndexDeserializer extends JsonDeserializer<HashMap> {
	
	public ComplaintIndexDeserializer() {
		super(HashMap.class);
	}

}
