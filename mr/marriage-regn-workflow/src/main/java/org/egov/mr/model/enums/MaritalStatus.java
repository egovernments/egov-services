package org.egov.mr.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MaritalStatus {
	UNMARRIED("UNMARRIED"),

	WIDOWED("WIDOWED"),

	DIVORCED("DIVORCED");

	private String value;

	MaritalStatus(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static MaritalStatus fromValue(String text) {
		for (MaritalStatus b : MaritalStatus.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}