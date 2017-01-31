package org.egov.web.notification.sms.transform;

import org.egov.web.notification.sms.model.SMSRequest;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class SmsRequestDeserializer extends JsonDeserializer<SMSRequest> {
	public SmsRequestDeserializer() {
		super(SMSRequest.class);
	}
}
