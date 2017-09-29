package org.egov.models;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.enums.Reason;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Yosadhara
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VacancyRemission {

	@NotNull
	@JsonProperty("upicNo")
	private String upicNo;

	@NotNull
	@Size(min = 4, max = 128)
	@JsonProperty("tenantId")
	private String tenantId;

	@JsonProperty("applicationNo")
	private String applicationNo;

	@NotNull
	@JsonProperty("fromDate")
	private String fromDate;

	@NotNull
	@JsonProperty("toDate")
	private String toDate;

	@NotNull
	@JsonProperty("percentage")
	private Double percentage;

	@NotNull
	@JsonProperty("reason")
	private Reason reason;

	@JsonProperty("requestDate")
	private String requestDate;

	@JsonProperty("approvedDate")
	private String approvedDate;

	@JsonProperty("isApproved")
	private Boolean isApproved;

	@NotNull
	@JsonProperty("documents")
	private List<Document> documents;

	@JsonProperty("remarks")
	private String remarks;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;

	@JsonProperty("workFlowDetails")
	private WorkFlowDetails workFlowDetails;
}
