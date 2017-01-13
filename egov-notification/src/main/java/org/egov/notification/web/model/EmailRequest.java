package org.egov.notification.web.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmailRequest {

	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo = null;

	@JsonProperty("email")
	private String email = null;

	@JsonProperty("subject")
	private String subject = null;

	@JsonProperty("body")
	private String body = null;

	@JsonProperty("sender")
	private String sender = null;

	public EmailRequest requestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
		return this;
	}

	public RequestInfo getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
	}

	public EmailRequest email(String email) {
		this.email = email;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public EmailRequest subject(String subject) {
		this.subject = subject;
		return this;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public EmailRequest body(String body) {
		this.body = body;
		return this;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public EmailRequest sender(String sender) {
		this.sender = sender;
		return this;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		EmailRequest emailRequest = (EmailRequest) o;
		return Objects.equals(this.requestInfo, emailRequest.requestInfo)
				&& Objects.equals(this.email, emailRequest.email) && Objects.equals(this.subject, emailRequest.subject)
				&& Objects.equals(this.body, emailRequest.body) && Objects.equals(this.sender, emailRequest.sender);
	}

	@Override
	public int hashCode() {
		return Objects.hash(requestInfo, email, subject, body, sender);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class EmailRequest {\n");

		sb.append("    requestInfo: ").append(toIndentedString(requestInfo)).append("\n");
		sb.append("    email: ").append(toIndentedString(email)).append("\n");
		sb.append("    subject: ").append(toIndentedString(subject)).append("\n");
		sb.append("    body: ").append(toIndentedString(body)).append("\n");
		sb.append("    sender: ").append(toIndentedString(sender)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}
