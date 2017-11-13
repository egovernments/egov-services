package org.egov.works.commons.web.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets EstimateSanctionType
 */
public enum EstimateSanctionType {

	FINANCIAL_SANCTION("FINANCIAL_SANCTION"),

	ADMINISTRATIVE_SANCTION("ADMINISTRATIVE_SANCTION"),

	TECHNICAL_SANCTION("TECHNICAL_SANCTION");

	private String value;

	EstimateSanctionType(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static EstimateSanctionType fromValue(String text) {
		for (EstimateSanctionType b : EstimateSanctionType.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
