package org.egov.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * ApplicationTypeEnum
 * 
 * Author : Pavan Kumar kamma
 */
public enum OwnerShipTypeEnum {

	STATE_GOVERNEMNT("State_Government"),

	RENTED("Rented");

	private String value;

	OwnerShipTypeEnum(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static OwnerShipTypeEnum fromValue(String text) {
		for (OwnerShipTypeEnum ownerShipTypeEnum : OwnerShipTypeEnum.values()) {
			if (String.valueOf(ownerShipTypeEnum.value).equals(text)) {
				return ownerShipTypeEnum;
			}
		}
		return null;
	}
}