package org.egov.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * type of the unit
 */
public enum UnitTypeEnum {
	FLAT("FLAT"),

	ROOM("ROOM");

	private String value;

	UnitTypeEnum(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static UnitTypeEnum fromValue(String text) {
		for (UnitTypeEnum b : UnitTypeEnum.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}