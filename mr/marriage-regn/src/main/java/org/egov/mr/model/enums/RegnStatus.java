package org.egov.mr.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RegnStatus {
	CREATED("CREATED"),

	REGISTERED("REGISTERED"),

	CANCELLED("CANCELLED");

	private String value;

	RegnStatus(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static RegnStatus fromValue(String text) {
		for (RegnStatus b : RegnStatus.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
