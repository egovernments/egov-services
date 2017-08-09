package org.egov.tl.commons.web.contract.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * RateTypeEnum
 * 
 * Author : Pavan Kumar kamma
 */
public enum RateTypeEnum {

	FLAT_BY_RANGE("FLAT_BY_RANGE"),

	FLAT_BY_PERCENTAGE("FLAT_BY_PERCENTAGE"),

	UNIT_BY_RANGE("UNIT_BY_RANGE");

	private String value;

	RateTypeEnum(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value).toUpperCase();
	}

	@JsonCreator
	public static RateTypeEnum fromValue(String text) {
		for (RateTypeEnum rateType : RateTypeEnum.values()) {
			if (String.valueOf(rateType.value).equals(text.toUpperCase())) {
				return rateType;
			}
		}
		return null;
	}
}