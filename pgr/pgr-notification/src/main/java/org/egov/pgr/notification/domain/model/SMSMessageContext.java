package org.egov.pgr.notification.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class SMSMessageContext {
    private String templateName;
    private Map<Object, Object> templateValues;
    private String mobileNumber;
}
