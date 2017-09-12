package org.egov.asset.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum VoucherType {

	JOURNALVOUCHER("Journal Voucher"), PAYMENT("PAYMENT"), RECEIPT("RECEIPT");

	private String value;

	VoucherType(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static VoucherType fromValue(String text) {
		for (VoucherType b : VoucherType.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
