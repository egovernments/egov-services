package org.egov.marriagefee.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Venue {
	RESIDENCE("RESIDENCE"),

	FUNCTION_HALL("FUNCTION_HALL"),

	WORSHIP_PLACE("WORSHIP_PLACE"),

	OTHERS("OTHERS");

	private String value;

	Venue(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static Venue fromValue(String text) {
		for (Venue b : Venue.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
