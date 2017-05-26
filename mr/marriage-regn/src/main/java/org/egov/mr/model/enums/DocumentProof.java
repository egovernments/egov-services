package org.egov.mr.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DocumentProof {
	COMMON("COMMON"),

	AGE("AGE"),

	ADDRESS_PROOF("ADDRESS_PROOF");

	private String value;

	DocumentProof(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static DocumentProof fromValue(String text) {
		for (DocumentProof b : DocumentProof.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
