package org.egov.pgr.employee.enrichment.consumer.contract;

import org.springframework.kafka.support.serializer.JsonDeserializer;

public class SevaRequestDeserializer extends JsonDeserializer<SevaRequest> {
    public SevaRequestDeserializer() {
        super(SevaRequest.class);
    }
}
