package org.egov.tl.commons.web.contract.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EstablishmentTypeEnum {
	
	SOLE_PROPRIETOR_SHIP("SOLE_PROPRIETOR_SHIP"),
	
	PARTNER_SHIP_FRIM("PARTNER_SHIP_FRIM"),
	
	PVT_LTD_CO("PVT_LTD_CO"),
	
	PUB_LTD_CO("PUB_LTD_CO"),
	
	TRUSTS("TRUSTS"),
	
	PUB_BODY("PUB_BODY"),

	BANK("BANK");

	private String value;

	EstablishmentTypeEnum(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value).toUpperCase();
	}

	@JsonCreator
	public static EstablishmentTypeEnum fromValue(String text) {
		for (EstablishmentTypeEnum establishment : EstablishmentTypeEnum.values()) {
			if (String.valueOf(establishment.value).equals(text.toUpperCase())) {
				return establishment;
			}
		}
		return null;
	}
}
