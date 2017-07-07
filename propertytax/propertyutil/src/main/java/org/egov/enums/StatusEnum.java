package org.egov.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * status of the Property
 */
public enum StatusEnum {
	ACTIVE("ACTIVE"),

	INACTIVE("INACTIVE"),

	WORKFLOW("WORKFLOW"),

	HISTORY("HISTORY"),

	CANCELED("CANCELED");

	private String value;

	StatusEnum(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static StatusEnum fromValue(String text) {
		for (StatusEnum b : StatusEnum.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}