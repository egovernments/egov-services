package org.egov.pgr.notification.persistence.queue.contract;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class SmsMessage {
    private String mobileNumber;
    private String message;
}
