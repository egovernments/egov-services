package org.egov.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Application type.
 */
public enum ApplicationEnum {
	CREATE("CREATE"), TITLE_TRANSFER("TITLE_TRANSFER"), BIFURCATION("BIFURCATION"), ALTER("ALTER"), EDIT(
			"EDIT"), AMALGAMATION("AMALGAMATION");

	private String value;

	ApplicationEnum(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static ApplicationEnum fromValue(String text) {
		for (ApplicationEnum b : ApplicationEnum.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
