package org.egov.notification.web.transform;

import org.egov.notification.web.model.SMSRequest;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class SmsRequestDeserializer extends JsonDeserializer<SMSRequest> {
    public SmsRequestDeserializer() {
        super(SMSRequest.class);
    }
}
