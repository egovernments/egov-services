package org.egov.web.notification.sms.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SMSRequest {
    @JsonProperty("mobileNumber")
    private String mobileNumber;
    @JsonProperty("message")
    private String message;
}
