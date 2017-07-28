package org.egov.collection.notification.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.egov.collection.notification.domain.model.EmailMessage;
import org.egov.collection.notification.domain.model.SmsMessage;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class EmailRequest {
    private String emailId;
    private String message;

    public EmailMessage toDomain() {
        return new EmailMessage(emailId, message);
    }
}
