package org.egov.works.measurementbook.common.queue;

import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Created by ramki on 11/11/17.
 */

@Service
public class HashMapDeserializer extends JsonDeserializer<HashMap> {

    public HashMapDeserializer() {
        super(HashMap.class);
    }
}

