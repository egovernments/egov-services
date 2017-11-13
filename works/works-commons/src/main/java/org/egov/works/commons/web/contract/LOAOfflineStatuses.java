package org.egov.works.commons.web.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets LOAOfflineStatuses
 */
public enum LOAOfflineStatuses {

	ACCEPTANCE_LETTER_ISSUED("ACCEPTANCE_LETTER_ISSUED"),

	ACCEPTANCE_LETTER_ACKNOWLEDGED("ACCEPTANCE_LETTER_ACKNOWLEDGED"),

	AGREEMENT_ORDER_SIGNED("AGREEMENT_ORDER_SIGNED");

	private String value;

	LOAOfflineStatuses(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static LOAOfflineStatuses fromValue(String text) {
		for (LOAOfflineStatuses b : LOAOfflineStatuses.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
