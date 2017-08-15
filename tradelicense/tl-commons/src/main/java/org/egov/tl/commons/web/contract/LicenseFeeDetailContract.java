package org.egov.tl.commons.web.contract;

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
public class LicenseFeeDetailContract {

	@JsonProperty("id")
	private Long id;

	@JsonProperty("licenseId")
	private Long licenseId;

	@NotNull
	@JsonProperty("financialYear")
	private String financialYear;

	@NotNull
	@JsonProperty("amount")
	private Double amount;

	@JsonProperty("paid")
	private Boolean paid = false;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;

}