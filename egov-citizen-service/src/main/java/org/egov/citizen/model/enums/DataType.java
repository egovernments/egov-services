package org.egov.citizen.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DataType {

	SINGLEVALUELIST("SINGLEVALUELIST"),MULTIVALUELIST("MULTIVALUELIST"), 
	STRING("STRING"), INT("INT"), DOUBLE("DOUBLE"), FLOAT("FLOAT"),
	DATE("DATE"), LONG("LONG"), BIGDECIMAL("BIGDECIMAL"),BOOLEAN("BOOLEAN");
	
	private String value;

	DataType(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static DataType fromValue(String text) {
		for (DataType b : DataType.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
