package org.egov.mr.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ApplicationStatus {
	
	ACTIVE("ACTIVE"),
	
	CREATED("CREATED"),

	REGISTERED("REGISTERED"),
	
	APPROVED("APPROVED"),
	
	REJECTED("REJECTED"),
	
	DIGITALSIGNED("DIGITALSIGNED"),

	CANCELLED("CANCELLED"),

	WORKFLOW("WORKFLOW");
	
	private String value;

	ApplicationStatus(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static ApplicationStatus fromValue(String text) {
		for (ApplicationStatus b : ApplicationStatus.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
