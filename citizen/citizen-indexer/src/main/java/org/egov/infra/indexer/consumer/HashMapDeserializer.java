package org.egov.infra.indexer.consumer;

import java.util.HashMap;

import org.springframework.kafka.support.serializer.JsonDeserializer;

public class HashMapDeserializer extends JsonDeserializer<HashMap> {

    public HashMapDeserializer() {
        super(HashMap.class);
    }


}
