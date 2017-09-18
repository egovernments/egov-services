package org.egov.lams.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Action {

	CREATE("CREATE"),

	CANCELLATION("CANCELLATION"),

	EVICTION("EVICTION"),

	RENEWAL("RENEWAL"),

	OBJECTION("OBJECTION"),

	JUDGEMENT("JUDGEMENT"),
	
	REMISSION("REMISSION");

	private String value;

	Action(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static Action fromValue(String text) {
		for (Action b : Action.values()) {
			if (String.valueOf(b.value).equalsIgnoreCase(text)) {
				return b;
			}
		}
		return null;
	}
}
