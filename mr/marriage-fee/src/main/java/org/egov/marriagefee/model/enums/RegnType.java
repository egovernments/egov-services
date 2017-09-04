package org.egov.marriagefee.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RegnType {
	REGISTRATION("REGISTRATION"),

	REISSUE("REISSUE");

	private String value;

	RegnType(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static RegnType fromValue(String text) {
		for (RegnType b : RegnType.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
