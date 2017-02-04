package org.egov.web.notification.sms.contract;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class SMSRequest {
    private String mobileNumber;
    private String message;
}
