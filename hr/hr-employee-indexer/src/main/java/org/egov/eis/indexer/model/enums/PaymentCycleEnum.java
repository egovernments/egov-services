package org.egov.eis.indexer.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentCycleEnum {

	MONTH("MONTH"), QUARTER("QUARTER"), HALFYEAR("HALFYEAR"), ANNUAL("ANNUAL");

	private String value;

	PaymentCycleEnum(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static PaymentCycleEnum fromValue(String text) {
		for (PaymentCycleEnum b : PaymentCycleEnum.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
