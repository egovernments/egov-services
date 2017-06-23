package org.egov.pgr.notification.domain.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class SMSRequest {
    private String message;
    private String mobileNumber;
}
