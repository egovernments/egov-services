package org.egov.lams.notification.web.contract;

import org.egov.lams.notification.model.Email;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
@Service
public class EmailRequest {
    private String email;
    private String subject;
    private String body;
  
    public Email toDomain() {
        return new Email(email, subject, body);
    }
}
