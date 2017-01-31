package org.egov.notification.web.transform;

import org.egov.notification.web.model.EmailRequest;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class EmailRequestDeserializer extends JsonDeserializer<EmailRequest> {
    public EmailRequestDeserializer() {
        super(EmailRequest.class);
    }
}
