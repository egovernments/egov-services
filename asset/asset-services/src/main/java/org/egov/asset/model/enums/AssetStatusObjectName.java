package org.egov.asset.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AssetStatusObjectName {

	ASSETMASTER("Asset Master"), REVALUATION("Revaluation"), DISPOSAL("Disposal");

	private String value;

	AssetStatusObjectName(final String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static AssetStatusObjectName fromValue(final String text) {
		for (final AssetStatusObjectName b : AssetStatusObjectName.values())
			if (String.valueOf(b.value).equals(text))
				return b;
		return null;
	}
}
