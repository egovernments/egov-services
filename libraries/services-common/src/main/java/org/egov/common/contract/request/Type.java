package org.egov.common.contract.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Type {
	
	EMPLOYEE("EMPLOYEE"), CITIZEN("CITIZEN"), SYSTEM("SYSTEM");
	
	private String value;

	Type(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
    public String toString() {
        return name();
    }
	
	@JsonCreator
	public static Type fromValue(String passedValue) {
		for (Type obj : Type.values()) {
			if (String.valueOf(obj.value).equals(passedValue.toUpperCase())) {
				return obj;
			}
		}
		return null;
	}
}
