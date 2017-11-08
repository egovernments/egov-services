package org.egov.works.commons.web.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets WorkProgress
 */
public enum WorkProgress {

	BEFORE("BEFORE"),

	DURING("DURING"),

	AFTER("AFTER");

	private String value;

	WorkProgress(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static WorkProgress fromValue(String text) {
		for (WorkProgress b : WorkProgress.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
