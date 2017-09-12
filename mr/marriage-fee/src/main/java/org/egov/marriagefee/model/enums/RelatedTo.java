package org.egov.marriagefee.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RelatedTo {
	BRIDE("BRIDE"),

	BRIDEGROOM("BRIDEGROOM");

	private String value;

	RelatedTo(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static RelatedTo fromValue(String text) {
		for (RelatedTo b : RelatedTo.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
