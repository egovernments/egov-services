package org.egov.works.commons.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Gets or Sets MilestoneStatus
 */
public enum MilestoneStatus {

	APPROVED("APPROVED"),

	CANCELLED("CANCELLED");

	private String value;

	MilestoneStatus(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static MilestoneStatus fromValue(String text) {
		for (MilestoneStatus b : MilestoneStatus.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
