package org.egov.mr.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ReligionPractice {

	BIRTH("BIRTH"),

	ADOPTION("ADOPTION");

	private String value;

	ReligionPractice(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static ReligionPractice fromValue(String text) {
		for (ReligionPractice b : ReligionPractice.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
