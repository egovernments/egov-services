package org.egov.tl.commons.web.contract.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * ApplicationTypeEnum
 * 
 * Author : Pavan Kumar kamma
 */
public enum OwnerShipTypeEnum {

	OWNED("OWNED"),

	RENTED("RENTED"),
	
	ULB("ULB"),
	
	STATE_GOVERNMENT("STATE_GOVERNMENT"),

	CENTRAL_GOVERNMENT("CENTRAL_GOVERNMENT");

	private String value;

	OwnerShipTypeEnum(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value).toUpperCase();
	}

	@JsonCreator
	public static OwnerShipTypeEnum fromValue(String text) {
		for (OwnerShipTypeEnum ownerShipTypeEnum : OwnerShipTypeEnum.values()) {
			if (String.valueOf(ownerShipTypeEnum.value).equals(text.toUpperCase())) {
				return ownerShipTypeEnum;
			}
		}
		return null;
	}
}