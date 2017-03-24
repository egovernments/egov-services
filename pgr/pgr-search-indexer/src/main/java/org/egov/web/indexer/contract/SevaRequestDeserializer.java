package org.egov.web.indexer.contract;

import org.springframework.kafka.support.serializer.JsonDeserializer;

public class SevaRequestDeserializer extends JsonDeserializer<SevaRequest> {

    public SevaRequestDeserializer() {
        super(SevaRequest.class);
    }

}
