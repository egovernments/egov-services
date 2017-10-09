package org.egov.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AssessmentDateEnum {
	FIRSTASSESSMENT("FIRSTASSESSMENT"), CURRENTASSESSMENT("CURRENTASSESSMENT"), REVISEDASSESSMENT(
			"REVISEDASSESSMENT"), LASTASSESSMENT("LASTASSESSMENT");

	private String value;

	AssessmentDateEnum(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static AssessmentDateEnum fromValue(String text) {
		for (AssessmentDateEnum b : AssessmentDateEnum.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
