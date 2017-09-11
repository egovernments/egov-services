package org.egov.citizen.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Action {

	BILLGENERATE("BILLGENERATE"),CREATEDEMAND("CREATEDEMAND");
	
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
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
	
}
