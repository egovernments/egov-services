package org.egov.web.notification.mail.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailRequest {

    @JsonProperty("email")
    private String email = null;

    @JsonProperty("subject")
    private String subject = null;

    @JsonProperty("body")
    private String body = null;

    @JsonProperty("sender")
    private String sender = null;

    public String getEmail() {
        return email;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public String getSender() {
        return sender;
    }
}
