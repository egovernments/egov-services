package org.egov.asset.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Sequence {
	CURRENTVALUESEQUENCE("seq_egasset_current_value"),DEPRECIATIONSEQUENCE("seq_egasset_depreciation");

	private String value;

	Sequence(final String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static Sequence fromValue(final String text) {
		for (final Sequence b : Sequence.values())
			if (String.valueOf(b.value).equals(text))
				return b;
		return null;
	}
}
