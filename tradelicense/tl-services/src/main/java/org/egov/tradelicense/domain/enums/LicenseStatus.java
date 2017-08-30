package org.egov.tradelicense.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LicenseStatus {
	APPROVED("Approved");

	private final String name;
}
