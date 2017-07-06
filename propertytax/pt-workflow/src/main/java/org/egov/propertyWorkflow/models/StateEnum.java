package org.egov.propertyWorkflow.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * StateEnum class
 * 
 * @author Pavan Kumar Kamma
 *
 */
public enum StateEnum {

	STARTED("STARTED"),

	INPROGRESS("INPROGRESS"),

	ENDED("ENDED");

	private String value;

	StateEnum(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static StateEnum fromValue(String text) {
		for (StateEnum state : StateEnum.values()) {
			if (String.valueOf(state.value).equals(text)) {
				return state;
			}
		}
		return null;
	}
}