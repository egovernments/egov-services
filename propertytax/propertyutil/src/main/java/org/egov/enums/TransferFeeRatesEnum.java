package org.egov.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * FeeFactor enum: The fee factor based on which the fee will be calculated
 * 
 * @author Yosadhara
 *
 */
public enum TransferFeeRatesEnum {

	PROPERTYTAX("PROPERTYTAX"),

	DOCUMENTVALUE("DOCUMENTVALUE"),

	MARKETVALUE("MARKETVALUE"),

	FLATRATE("FLATRATE");

	private String value;

	TransferFeeRatesEnum(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static TransferFeeRatesEnum fromValue(String text) {
		for (TransferFeeRatesEnum transferFeeRates : TransferFeeRatesEnum.values()) {
			if (String.valueOf(transferFeeRates.value).equals(text)) {
				return transferFeeRates;
			}
		}
		return null;
	}
}
