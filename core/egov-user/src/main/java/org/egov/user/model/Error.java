package org.egov.user.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Error {
	@JsonProperty("code")
	private Integer code = null;

	@JsonProperty("message")
	private String message = null;

	@JsonProperty("description")
	private String description = null;

	@JsonProperty("fields")
	private Object fields = null;

	public Error code(Integer code) {
		this.code = code;
		return this;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Error message(String message) {
		this.message = message;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Error description(String description) {
		this.description = description;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Error fields(Object fields) {
		this.fields = fields;
		return this;
	}

	public Object getFields() {
		return fields;
	}

	public void setFields(Object fields) {
		this.fields = fields;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Error error = (Error) o;
		return Objects.equals(this.code, error.code) && Objects.equals(this.message, error.message)
				&& Objects.equals(this.description, error.description) && Objects.equals(this.fields, error.fields);
	}

	@Override
	public int hashCode() {
		return Objects.hash(code, message, description, fields);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Error {\n");

		sb.append("    code: ").append(toIndentedString(code)).append("\n");
		sb.append("    message: ").append(toIndentedString(message)).append("\n");
		sb.append("    description: ").append(toIndentedString(description)).append("\n");
		sb.append("    fields: ").append(toIndentedString(fields)).append("\n");
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
