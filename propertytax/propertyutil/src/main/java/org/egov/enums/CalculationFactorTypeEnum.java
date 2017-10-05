package org.egov.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * CalculationFactorTypeEnum - type of calculation factor
 * 
 * Author : Pavan Kumar kamma
 */
public enum CalculationFactorTypeEnum {

	OCCUPANCY("occupancy"),

	USAGE("usage"),

	STRUCTURE("structure"),

	PROPERTYTYPE("propertytype"),

	AGE("age"), TOILET("TOILET"), ROAD("ROAD"), LIFT("LIFT"), PARKING("PARKING");

	private String value;

	CalculationFactorTypeEnum(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static CalculationFactorTypeEnum fromValue(String text) {
		for (CalculationFactorTypeEnum factorType : CalculationFactorTypeEnum.values()) {
			if (String.valueOf(factorType.value).equals(text)) {
				return factorType;
			}
		}
		return null;
	}
}
