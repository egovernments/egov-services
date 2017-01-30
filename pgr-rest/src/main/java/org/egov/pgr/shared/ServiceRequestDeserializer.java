package org.egov.pgr.shared;

import org.egov.pgr.model.ServiceRequest;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class ServiceRequestDeserializer extends JsonDeserializer<ServiceRequest> {
    public ServiceRequestDeserializer() {
        super(ServiceRequest.class);
    }
}
