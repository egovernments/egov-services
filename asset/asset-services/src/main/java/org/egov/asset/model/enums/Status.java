package org.egov.asset.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
	CREATED("CREATED"), CAPITALIZED("CAPITALIZED"), CANCELLED("CANCELLED"), DISPOSED("DISPOSED"), APPROVED("APPROVED");

	private String value;

	Status(final String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static Status fromValue(final String text) {
		for (final Status b : Status.values())
			if (String.valueOf(b.value).equals(text))
				return b;
		return null;
	}
}
