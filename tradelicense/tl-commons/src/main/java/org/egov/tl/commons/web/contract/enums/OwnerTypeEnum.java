package org.egov.tl.commons.web.contract.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OwnerTypeEnum {
	
	INDIVIDUAL("INDIVIDUAL"),

	ORGANIZATION("ORGANIZATION");

	private String value;

	OwnerTypeEnum(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value).toUpperCase();
	}

	@JsonCreator
	public static OwnerTypeEnum fromValue(String text) {
		for (OwnerTypeEnum ownerType : OwnerTypeEnum.values()) {
			if (String.valueOf(ownerType.value).equals(text.toUpperCase())) {
				return ownerType;
			}
		}
		return null;
	}
}
