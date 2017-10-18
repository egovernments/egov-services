package org.egov.models;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A Object holds the basic data for a property
 */

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TaxExemption {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("tenantId")
	@NotNull
	@Size(min = 4, max = 128)
	private String tenantId = null;

	@JsonProperty("upicNumber")
	@Size(min = 6, max = 128)
	private String upicNumber = null;
	
	@JsonProperty("applicationNo")
	@Size(min = 1, max = 64)
	private String applicationNo = null;

	@JsonProperty("exemptionReason")
	@NotNull
	private String exemptionReason = null;

	@JsonProperty("exemptionPercentage")
	private Double exemptionPercentage = null;

	@JsonProperty("documents")
	private List<Document> documents = new ArrayList<Document>();

	@JsonProperty("comments")
	@Size(max = 256)
	private String comments = null;

	@JsonProperty("stateId")
	private String stateId = null;
	
	@JsonProperty("workFlowDetails")
	private WorkFlowDetails workFlowDetails = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

}
