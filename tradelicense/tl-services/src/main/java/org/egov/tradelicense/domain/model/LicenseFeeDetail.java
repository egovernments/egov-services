package org.egov.tradelicense.domain.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LicenseFeeDetail {

	@JsonProperty("id")
	private Long id;

//	@JsonProperty("licenseId")
//	private Long licenseId;

	@NotNull
	@JsonProperty("financialYear")
	private String financialYear;

	@JsonProperty("tenantId")
	private String tenantId;
	
	@JsonProperty("applicationId")
	private Long applicationId;
	
	@NotNull
	@JsonProperty("amount")
	private Double amount;

	@JsonProperty("paid")
	private Boolean paid = false;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;

}