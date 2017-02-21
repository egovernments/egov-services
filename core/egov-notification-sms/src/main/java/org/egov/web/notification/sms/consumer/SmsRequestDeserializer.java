package org.egov.web.notification.sms.consumer;

import org.egov.web.notification.sms.consumer.contract.SMSRequest;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class SmsRequestDeserializer extends JsonDeserializer<SMSRequest> {
    public SmsRequestDeserializer() {
        super(SMSRequest.class);
    }
}
