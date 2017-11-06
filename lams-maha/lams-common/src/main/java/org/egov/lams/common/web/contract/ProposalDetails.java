package org.egov.lams.common.web.contract;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProposalDetails {
	private Long id;
	@NotNull
	private String tenantId;
	private String adhaarNumber;
	@NotNull
	private Integer proposalDate;
	
	private String proposalNumber;
	
	@NotNull
	private String proposerName;
	@NotNull
	private String proposerDepartment;
	@NotNull
	private String landOwnerName;
	@NotNull
	private String purposeOfLandAcquisition;
	private String organizationName;
	private String contactNumber;
	private String emailId;
	private String mobileNumber;

}
