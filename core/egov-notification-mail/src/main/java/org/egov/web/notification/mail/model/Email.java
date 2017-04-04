package org.egov.web.notification.mail.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Email {
    private String toAddress;
    private String subject;
    private String body;
}
