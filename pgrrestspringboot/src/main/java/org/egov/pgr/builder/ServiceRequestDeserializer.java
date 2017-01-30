package org.egov.pgr.builder;

import org.egov.pgr.model.ServiceRequest;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class ServiceRequestDeserializer extends JsonDeserializer<ServiceRequest> {
    public ServiceRequestDeserializer() {
        super(ServiceRequest.class);
    }
}
