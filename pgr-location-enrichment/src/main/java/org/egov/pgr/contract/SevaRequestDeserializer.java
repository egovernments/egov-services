package org.egov.pgr.contract;

import org.egov.pgr.model.SevaRequest;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class SevaRequestDeserializer extends JsonDeserializer<SevaRequest> {
    public SevaRequestDeserializer() {
        super(SevaRequest.class);
    }
}
