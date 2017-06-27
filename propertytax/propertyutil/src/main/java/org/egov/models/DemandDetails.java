package org.egov.models;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DemandDetails {

	private Integer id;

	@JsonProperty("taxHead")
	private TaxHeadMaster taxHead;

	@NotNull
	private String taxHeadCode;

	@NotNull
	private Double taxAmount;

	@NotNull
	private Double collectionAmount;

	private AuditDetails auditDetails;

	@NotNull
	private String tenantId;

}
