package org.egov.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Source of a assessment data. The properties will be created in a system based
 * on the data avaialble in their manual records or during field survey. There
 * can be more from client to client.
 */
public enum SourceEnum {
	MUNICIPAL_RECORDS("MUNICIPAL_RECORDS"),

	FIELD_SURVEY("FIELD_SURVEY");

	private String value;

	SourceEnum(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static SourceEnum fromValue(String text) {
		for (SourceEnum b : SourceEnum.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}