package org.egov.lams.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ReasonForCancellation {

	TERMINATION("Termination"), CANCELLATION("Cancellation");

	private String value;

	ReasonForCancellation(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static ReasonForCancellation fromValue(String text) {
		for (ReasonForCancellation b : ReasonForCancellation.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
