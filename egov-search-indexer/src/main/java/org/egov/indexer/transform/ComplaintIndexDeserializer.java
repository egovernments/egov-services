package org.egov.indexer.transform;

import java.util.HashMap;

import org.egov.indexer.entity.ComplaintIndex;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class ComplaintIndexDeserializer extends JsonDeserializer<HashMap> {
	
	public ComplaintIndexDeserializer() {
		super(HashMap.class);
	}

}
