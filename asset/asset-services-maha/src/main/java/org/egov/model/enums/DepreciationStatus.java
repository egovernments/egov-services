package org.egov.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DepreciationStatus {

	SUCCESS("SUCCESS"),

	FAIL("FAIL");

	private String value;

	DepreciationStatus(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static DepreciationStatus fromValue(String text) {

		for (DepreciationStatus b : DepreciationStatus.values())
			if (String.valueOf(b.value).equals(text))
				return b;
		
		return null;
	}

}
