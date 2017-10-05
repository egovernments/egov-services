package org.egov.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * VacancyRemission enum: This is for Vacancy Remission reason
 * 
 * @author Yosadhara
 *
 */
public enum Reason {

	NINTY_DAYS_INACTIVE("NINTY_DAYS_INACTIVE"),

	FULLY_DEMOLISHED("FULLY_DEMOLISHED"),

	PARTIALLY_DEMOLISHED("PARTIALLY_DEMOLISHED"),

	DESTROYED_BY_FIRE("DESTROYED_BY_FIRE");

	private String value;

	Reason(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static Reason fromValue(String text) {
		for (Reason vacancyRemission : Reason.values()) {
			if (String.valueOf(vacancyRemission.value).equals(text)) {
				return vacancyRemission;
			}
		}
		return null;
	}

}
