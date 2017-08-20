package org.egov.tl.indexer.config;

import java.util.HashMap;

import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Service;

@Service
public class HashMapDeserializer extends JsonDeserializer<HashMap> {

	public HashMapDeserializer() {
		super(HashMap.class);
	}
}
