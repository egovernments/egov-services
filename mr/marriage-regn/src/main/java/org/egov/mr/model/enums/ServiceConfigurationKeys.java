package org.egov.mr.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ServiceConfigurationKeys {

	MARRIAGEBATCHSIZE("MarriageBatchSize"),
	
	MARRIAGEWITNESSSIZE("MarriageWittnessSize");

	private String value;

	ServiceConfigurationKeys(final String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static ServiceConfigurationKeys fromValue(final String text) {
		for (final ServiceConfigurationKeys b : ServiceConfigurationKeys.values())
			if (String.valueOf(b.value).equals(text))
				return b;
		return null;
	}

}
