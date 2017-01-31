package org.egov.web.notification.mail.transform;

import org.egov.web.notification.mail.model.EmailRequest;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class EmailRequestDeserializer extends JsonDeserializer<EmailRequest> {
	public EmailRequestDeserializer() {
		super(EmailRequest.class);
	}
}
