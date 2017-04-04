package org.egov.web.notification.mail.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Email {
    private String toAddress;
    private String subject;
    private String body;
}
