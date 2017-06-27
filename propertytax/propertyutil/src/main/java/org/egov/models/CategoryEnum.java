package org.egov.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 
 * @author Prasad
 *
 */
public enum CategoryEnum {

	TAX("TAX"),
	FEE("FEE"),
	REBATE("REBATE"),
	ADVANCE_COLLECTION("ADVANCE_COLLECTION"),
	PENALTY("PENALTY"),
	FINES("FINES");
	
	
	private String value;
	
	CategoryEnum(String value) {
		this.value = value;
	}
	
	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static CategoryEnum fromValue(String text) {
		for (CategoryEnum b : CategoryEnum.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
