package org.egov.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * RateTypeEnum
 * 
 * Author : Pavan Kumar kamma
 */
public enum RateTypeEnum {

	FLAT_BY_RANGE("Flat_By_Range"),

	FLAT_BY_PERCENTAGE("Flat_By_Percentage"),

	UNIT_BY_RANGE("Unit_By_Range");

	private String value;

	RateTypeEnum(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static RateTypeEnum fromValue(String text) {
		for (RateTypeEnum rateType : RateTypeEnum.values()) {
			if (String.valueOf(rateType.value).equals(text)) {
				return rateType;
			}
		}
		return null;
	}
}