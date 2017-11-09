package org.egov.works.estimate.persistence.helper;

import java.math.BigDecimal;

import org.egov.works.estimate.web.contract.AbstractEstimateDetails;
import org.egov.works.estimate.web.contract.AuditDetails;
import org.egov.works.estimate.web.contract.ProjectCode;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AbstractEstimateDetailsHelper {
	@JsonProperty("id")
	private String id = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("abstractEstimate")
	private String abstractEstimate = null;

	@JsonProperty("nameOfWork")
	private String nameOfWork = null;

	@JsonProperty("estimateAmount")
	private BigDecimal estimateAmount = null;

	@JsonProperty("grossAmountBilled")
	private Double grossAmountBilled = null;

	@JsonProperty("projectCode")
	private String projectCode = null;

	@JsonProperty("createdBy")
	private String createdBy = null;

	@JsonProperty("lastModifiedBy")
	private String lastModifiedBy = null;

	@JsonProperty("createdTime")
	private Long createdTime = null;

	@JsonProperty("lastModifiedTime")
	private Long lastModifiedTime = null;

	public AbstractEstimateDetails toDomain() {
		AbstractEstimateDetails estimateDetails = new AbstractEstimateDetails();
		estimateDetails.setAbstractEstimate(this.abstractEstimate);
		estimateDetails.setAuditDetails(new AuditDetails());
		estimateDetails.getAuditDetails().setCreatedBy(this.createdBy);
		estimateDetails.getAuditDetails().setCreatedTime(this.createdTime);
		estimateDetails.getAuditDetails().setLastModifiedBy(this.lastModifiedBy);
		estimateDetails.getAuditDetails().setLastModifiedTime(this.lastModifiedTime);
		estimateDetails.setEstimateAmount(this.estimateAmount);
		estimateDetails.setGrossAmountBilled(this.grossAmountBilled);
		estimateDetails.setId(this.id);
		estimateDetails.setNameOfWork(this.nameOfWork);
		if(this.projectCode != null) {
			estimateDetails.setProjectCode(new ProjectCode());
			estimateDetails.getProjectCode().setCode(this.projectCode);
		}
		estimateDetails.setTenantId(this.tenantId);
		return estimateDetails;
	}

}
