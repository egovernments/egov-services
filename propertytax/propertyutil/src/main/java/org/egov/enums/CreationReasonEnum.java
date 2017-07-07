package org.egov.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * New property comes into system either property is newly constructed or
 * existing property got sub divided. Here the reason for creation will be
 * captured.
 */
public enum CreationReasonEnum {
	NEWPROPERTY("NEWPROPERTY"),

	SUBDIVISION("SUBDIVISION");

	private String value;

	CreationReasonEnum(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static CreationReasonEnum fromValue(String text) {
		for (CreationReasonEnum b : CreationReasonEnum.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
