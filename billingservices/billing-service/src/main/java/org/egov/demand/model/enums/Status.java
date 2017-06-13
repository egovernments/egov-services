package org.egov.demand.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Status {

	CREATED("CREATED"),

	CANCELLED("CANCELLED"),

	INSTRUMENT_BOUNCED("INSTRUMENT_BOUNCED");

	private String value;

	Status(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static Status fromValue(String text) {
		for (Status b : Status.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
