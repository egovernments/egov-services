package org.egov.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * FeeTypeEnum
 * 
 * Author : Pavan Kumar kamma
 */
public enum FeeTypeEnum {

	LICENSE("License"),

	MOTOR("Motor"),

	WORKFORCE("Workforce");

	private String value;

	FeeTypeEnum(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static FeeTypeEnum fromValue(String text) {
		for (FeeTypeEnum feeType : FeeTypeEnum.values()) {
			if (String.valueOf(feeType.value).equals(text)) {
				return feeType;
			}
		}
		return null;
	}
}