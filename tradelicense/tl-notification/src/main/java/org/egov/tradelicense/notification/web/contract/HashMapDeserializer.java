package org.egov.tradelicense.notification.web.contract;

import java.util.HashMap;

import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Service;

@Service
public class HashMapDeserializer extends JsonDeserializer<HashMap> {

	public HashMapDeserializer() {
		super(HashMap.class);
	}
}
