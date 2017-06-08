package org.egov.lams.notification.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum NatureOfAllotment {

	TENDER("TENDER"), DIRECT("DIRECT"), AUCTION("AUCTION");

	private String value;

	NatureOfAllotment(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static NatureOfAllotment fromValue(String text) {
		for (NatureOfAllotment b : NatureOfAllotment.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}

}
