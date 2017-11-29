package org.egov.lcms.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EntryType {
	
	SUMMON("SUMMON"),
	WARRANT("WARRANT");

	private String value;

	EntryType(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return name().toUpperCase();
	}

	@JsonCreator
	public static EntryType fromValue(String text) {
		for (EntryType entryType : EntryType.values()) {
			if (String.valueOf(entryType.value).equals(text)) {
				return entryType;
			}
		}
		return null;
	}
}
