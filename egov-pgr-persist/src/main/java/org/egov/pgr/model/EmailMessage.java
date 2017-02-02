package org.egov.pgr.model;

public class EmailMessage {

    private String email;
    private String subject;
    private String body;
    private String sender;

    public EmailMessage(String email, String subject, String body, String sender) {
        this.email = email;
        this.subject = subject;
        this.body = body;
        this.sender = sender;
    }

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
