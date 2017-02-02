package org.egov.web.notification.sms.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SMSRequest {
    private String mobileNumber;
    private String message;
}
