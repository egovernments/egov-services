package org.egov.tradelicense.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LicenseStatus {
	INFORCE("Inforce"),
	EXPIRED("Expired");

	private final String name;
}
