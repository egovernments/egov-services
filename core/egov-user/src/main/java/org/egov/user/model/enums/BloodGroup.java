package org.egov.user.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;

@Getter
public enum BloodGroup {
    A_POSITIVE("A+"),
    B_POSITIVE("B+"),
    O_POSITIVE("O+"),
    AB_POSITIVE("AB+"),
    A_NEGATIVE("A-"),
    B_NEGATIVE("B-"),
    AB_NEGATIVE("AB-"),
    O_NEGATIVE("O-");

	private String value;

	BloodGroup(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static BloodGroup fromValue(String text) {
		for (BloodGroup b : BloodGroup.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
