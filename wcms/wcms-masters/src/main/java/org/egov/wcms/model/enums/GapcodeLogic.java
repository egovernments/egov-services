package org.egov.wcms.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum GapcodeLogic {

	HIGHEST("HIGHEST"), LOWEST("LOWEST"), AVERAGE("AVERAGE");

	private final String value;

	GapcodeLogic(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}


	@JsonCreator
	public static GapcodeLogic fromValue(final String passedValue) {
		for (final GapcodeLogic obj : GapcodeLogic.values())
			if (String.valueOf(obj.value).equals(passedValue.toUpperCase()))
				return obj;
		return null;
	}


}
