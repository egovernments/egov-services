package org.egov.works.commons.web.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets ContractorAdvanceStatus
 */
public enum ContractorAdvanceStatus {

	NEW("NEW"),

	CREATED("CREATED"),

	CHECKED("CHECKED"),

	APPROVED("APPROVED"),

	REJECTED("REJECTED"),

	RESUBMITTED("RESUBMITTED"),

	CANCELLED("CANCELLED");

	private String value;

	ContractorAdvanceStatus(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static ContractorAdvanceStatus fromValue(String text) {
		for (ContractorAdvanceStatus b : ContractorAdvanceStatus.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
