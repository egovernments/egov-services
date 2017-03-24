package org.egov.eis.indexer.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum NatureOfAllotmentEnum {

	TENDER("TENDER"), DIRECT("DIRECT"), AUCTION("AUCTION");

	private String value;

	NatureOfAllotmentEnum(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static NatureOfAllotmentEnum fromValue(String text) {
		for (NatureOfAllotmentEnum b : NatureOfAllotmentEnum.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}

}
