package org.egov.works.masters.common.queue;

import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Created by ramki on 2/11/17.
 */

@Service
public class HashMapDeserializer extends JsonDeserializer<HashMap> {

    public HashMapDeserializer() {
        super(HashMap.class);
    }
}

