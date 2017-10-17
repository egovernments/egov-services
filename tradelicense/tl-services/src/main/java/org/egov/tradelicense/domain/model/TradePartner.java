package org.egov.tradelicense.domain.model;

import org.egov.tradelicense.domain.enums.Gender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradePartner {

	private Long id;

	private String tenantId;

	private Long licenseId;

	private String aadhaarNumber;

	private String fullName;

	private Gender gender;

	private String birthYear;

	private String emailId;

	private String designation;

	private String residentialAddress;

	private String correspondenceAddress;

	private String phoneNumber;

	private String mobileNumber;

	private String photo;

	private AuditDetails auditDetails;
}
