package org.egov.web.notification.sms.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SMSRequest {

	@JsonProperty("mobileNumber")
	private String mobileNumber = null;

	@JsonProperty("message")
	private String message = null;

	public String getMobileNumber() {
		return mobileNumber;
	}

	public String getMessage() {
		return message;
	}
}
