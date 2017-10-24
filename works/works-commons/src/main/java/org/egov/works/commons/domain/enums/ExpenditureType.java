package org.egov.works.commons.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Gets or Sets ExpenditureType
 */
public enum ExpenditureType {

	CAPITAL("CAPITAL"),

	REVENUE("REVENUE"),

	OTHERS("OTHERS");

	private String value;

	ExpenditureType(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static ExpenditureType fromValue(String text) {
		for (ExpenditureType b : ExpenditureType.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
