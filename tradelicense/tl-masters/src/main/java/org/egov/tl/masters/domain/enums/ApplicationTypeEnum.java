package org.egov.tl.masters.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * ApplicationTypeEnum
 * 
 * Author : Pavan Kumar kamma
 */
public enum ApplicationTypeEnum {

	NEW("NEW"),

	RENEW("RENEW");

	private String value;

	ApplicationTypeEnum(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value).toUpperCase();
	}

	@JsonCreator
	public static ApplicationTypeEnum fromValue(String text) {
		for (ApplicationTypeEnum applicationTypeEnum : ApplicationTypeEnum.values()) {
			if (String.valueOf(applicationTypeEnum.value).equals(text.toUpperCase())) {
				return applicationTypeEnum;
			}
		}
		return null;
	}
}