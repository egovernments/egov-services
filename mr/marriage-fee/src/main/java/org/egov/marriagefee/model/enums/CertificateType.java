package org.egov.marriagefee.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CertificateType {
	REGISTRATION("REGISTRATION"),

	REISSUE("REISSUE"),

	REJECTION("REJECTION");

	private String value;

	CertificateType(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static CertificateType fromValue(String text) {
		for (CertificateType b : CertificateType.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
