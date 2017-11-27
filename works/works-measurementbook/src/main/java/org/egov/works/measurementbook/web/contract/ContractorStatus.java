package org.egov.works.measurementbook.web.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets ContractorStatus
 */
public enum ContractorStatus {

	ACTIVE("ACTIVE"),

	INACTIVE("INACTIVE"),

	BLACK_LISTED("BLACK_LISTED"),

	DEBARRED("DEBARRED");

	private String value;

	ContractorStatus(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static ContractorStatus fromValue(String text) {
		for (ContractorStatus b : ContractorStatus.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
