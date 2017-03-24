package org.egov.asset.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ModeOfAcquisition {
	
	ACQUIRED("ACQUIRED"), CONSTRUCTION("CONSTRUCTION"), PURCHASE("PURCHASE"), TENDER("TENDER"), DONATION("DONATION");

	private String value;

	ModeOfAcquisition(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static ModeOfAcquisition fromValue(String text) {
		for (ModeOfAcquisition b : ModeOfAcquisition.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
