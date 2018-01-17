package org.egov.lams.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BasisOfAllotment {

	GODDWILLAUCTIONBASIS("GOODWILLAUCTIONBASIS"), NORMAL("NORMALBASIS");

	private String value;

	BasisOfAllotment(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static BasisOfAllotment fromValue(String text) {
		for (BasisOfAllotment b : BasisOfAllotment.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}

}
