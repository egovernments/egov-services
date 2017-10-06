package org.egov.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum FactorEnum {
	TOILET("TOILET"), ROAD("ROAD"), LIFT("LIFT"), PARKING("PARKING");

	private String value;

	FactorEnum(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static FactorEnum fromValue(String text) {
		for (FactorEnum b : FactorEnum.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
