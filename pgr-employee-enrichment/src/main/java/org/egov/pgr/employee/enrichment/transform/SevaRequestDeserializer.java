package org.egov.pgr.employee.enrichment.transform;

import org.egov.pgr.employee.enrichment.model.SevaRequest;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class SevaRequestDeserializer extends JsonDeserializer<SevaRequest> {
    public SevaRequestDeserializer() {
        super(SevaRequest.class);
    }
}
