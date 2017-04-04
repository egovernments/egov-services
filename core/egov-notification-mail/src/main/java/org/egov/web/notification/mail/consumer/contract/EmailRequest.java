package org.egov.web.notification.mail.consumer.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egov.web.notification.mail.model.Email;

@AllArgsConstructor
@Getter
public class EmailRequest {
    private String email;
    private String subject;
    private String body;
    private String sender;

    public Email toDomain() {
        return new Email(email, subject, body);
    }
}
