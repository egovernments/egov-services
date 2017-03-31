package org.egov.pgr.write.transform;

import org.egov.pgr.write.contracts.grievance.SevaRequest;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class SevaRequestDeserializer extends JsonDeserializer<SevaRequest> {

    public SevaRequestDeserializer() {
        super(SevaRequest.class);
    }
}
