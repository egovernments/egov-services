package org.egov.user.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DbAction {

	INSERT("INSERT"), UPDATE("UPDATE");
	
	private String value;

	DbAction(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static DbAction fromValue(String text) {
		for (DbAction b : DbAction.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
