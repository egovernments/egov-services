package org.egov.works.commons.web.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets RevisionType
 */
public enum RevisionType {

	NON_TENDERED_ITEM("NON_TENDERED_ITEM"),

	LUMP_SUM_ITEM("LUMP_SUM_ITEM"),

	ADDITIONAL_QUANTITY("ADDITIONAL_QUANTITY"),

	REDUCED_QUANTITY("REDUCED_QUANTITY");

	private String value;

	RevisionType(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static RevisionType fromValue(String text) {
		for (RevisionType b : RevisionType.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
