package org.egov.pgr.transform;

import org.egov.pgr.contracts.grievance.SevaRequest;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class SevaRequestDeserializer extends JsonDeserializer<SevaRequest> {

    public SevaRequestDeserializer() {
        super(SevaRequest.class);
    }
}
