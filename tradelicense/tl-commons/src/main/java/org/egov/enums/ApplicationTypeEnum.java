package org.egov.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * ApplicationTypeEnum
 * 
 * Author : Pavan Kumar kamma
 */
public enum ApplicationTypeEnum {

	NEW("New"),

	RENEW("Renew");

	private String value;

	ApplicationTypeEnum(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static ApplicationTypeEnum fromValue(String text) {
		for (ApplicationTypeEnum applicationTypeEnum : ApplicationTypeEnum.values()) {
			if (String.valueOf(applicationTypeEnum.value).equals(text)) {
				return applicationTypeEnum;
			}
		}
		return null;
	}
}