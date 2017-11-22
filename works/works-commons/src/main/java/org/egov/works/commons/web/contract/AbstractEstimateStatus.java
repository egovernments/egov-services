package org.egov.works.commons.web.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets AbstractEstimateStatus
 */
public enum AbstractEstimateStatus {

	NEW("NEW"),

	CREATED("CREATED"),

	ADMIN_SANCTIONED("ADMIN_SANCTIONED"),

	REJECTED("REJECTED"),

	CANCELLED("CANCELLED"),

	APPROVED("APPROVED"),

	RESUBMITTED("RESUBMITTED"),

	CHECKED("CHECKED"),

	FINANCIAL_SANCTIONED("FINANCIAL_SANCTIONED");

	private String value;

	AbstractEstimateStatus(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static AbstractEstimateStatus fromValue(String text) {
		for (AbstractEstimateStatus b : AbstractEstimateStatus.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
