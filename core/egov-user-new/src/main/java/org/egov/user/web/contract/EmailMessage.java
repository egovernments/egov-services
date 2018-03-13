package org.egov.user.web.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@AllArgsConstructor
@Getter
@ToString
public class EmailMessage {
    private String email;
    private String subject;
    private String body;
}
