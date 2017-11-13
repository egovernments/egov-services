package org.egov.lcms.notification.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


public enum VocherType {

	PAYMENT("Payment"),
	
	RECEIPT("Receipt");

	private String value;

	VocherType(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return name().toUpperCase();
	}

	@JsonCreator
	public static VocherType fromValue(String text) {
		for (VocherType vocherType : VocherType.values()) {
			if (String.valueOf(vocherType.value).equals(text)) {
				return vocherType;
			}
		}
		return null;
	}
	
}
