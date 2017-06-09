package org.egov.lams.notification.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentCycle {

	MONTH("MONTH"), QUARTER("QUARTER"), HALFYEAR("HALFYEAR"), ANNUAL("ANNUAL");

	private String value;

	PaymentCycle(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static PaymentCycle fromValue(String text) {
		for (PaymentCycle b : PaymentCycle.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
