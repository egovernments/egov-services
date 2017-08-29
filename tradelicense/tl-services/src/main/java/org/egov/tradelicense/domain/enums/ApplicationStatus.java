package org.egov.tradelicense.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplicationStatus {

	ACKNOWLEDGED("Acknowledged"), APPLICATION_FEE_PAID("Application Fee paid"), SCRUTINY_COMPLETED(
			"Scrutiny Completed"), INSPECTION_COMPLETED("Inspection Completed"), FINAL_APPROVAL_COMPLETED(
					"Final Approval Completed"), LICENSE_FEE_PAID("License Fee Paid"), LICENSE_ISSUED("License Issued");

	private final String name;

}