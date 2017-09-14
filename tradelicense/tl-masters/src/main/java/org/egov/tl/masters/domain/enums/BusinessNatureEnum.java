package org.egov.tl.masters.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * BusinessNatureEnum
 * 
 * Author : Pavan Kumar kamma
 */
public enum BusinessNatureEnum {

	PERMANENT("PERMANENT"),

	TEMPORARY("TEMPORARY");

	private String value;

	BusinessNatureEnum(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value).toUpperCase();
	}

	@JsonCreator
	public static BusinessNatureEnum fromValue(String text) {
		for (BusinessNatureEnum businessNature : BusinessNatureEnum.values()) {
			if (String.valueOf(businessNature.value).equals(text.toUpperCase())) {
				return businessNature;
			}
		}
		return null;
	}
}