package org.egov.works.estimate.web.model;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.works.commons.domain.model.AuditDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * An Object that hold Estimate Appropriation for a given Abstract Estimate
 * Details
 */
@ApiModel(description = "An Object that hold Estimate Appropriation for a given Abstract Estimate Details")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-24T10:20:21.690Z")

public class EstimateAppropriation {
	@JsonProperty("id")
	private String id = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("abstractEstimateDetails")
	private AbstractEstimateDetails abstractEstimateDetails = null;

	@JsonProperty("detailedEstimate")
	private DetailedEstimate detailedEstimate = null;

	@JsonProperty("budgetUsage")
	private String budgetUsage = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

	public EstimateAppropriation id(String id) {
		this.id = id;
		return this;
	}

	/**
	 * Unique Identifier of the Estimate Appropriation
	 * 
	 * @return id
	 **/
	@ApiModelProperty(value = "Unique Identifier of the Estimate Appropriation")

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public EstimateAppropriation tenantId(String tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	/**
	 * Tenant id for which this object belongs to
	 * 
	 * @return tenantId
	 **/
	@ApiModelProperty(required = true, value = "Tenant id for which this object belongs to")
	@NotNull

	@Size(min = 4, max = 128)
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public EstimateAppropriation abstractEstimateDetails(AbstractEstimateDetails abstractEstimateDetails) {
		this.abstractEstimateDetails = abstractEstimateDetails;
		return this;
	}

	/**
	 * Get abstractEstimateDetails
	 * 
	 * @return abstractEstimateDetails
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public AbstractEstimateDetails getAbstractEstimateDetails() {
		return abstractEstimateDetails;
	}

	public void setAbstractEstimateDetails(AbstractEstimateDetails abstractEstimateDetails) {
		this.abstractEstimateDetails = abstractEstimateDetails;
	}

	public EstimateAppropriation detailedEstimate(DetailedEstimate detailedEstimate) {
		this.detailedEstimate = detailedEstimate;
		return this;
	}

	/**
	 * Get detailedEstimate
	 * 
	 * @return detailedEstimate
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public DetailedEstimate getDetailedEstimate() {
		return detailedEstimate;
	}

	public void setDetailedEstimate(DetailedEstimate detailedEstimate) {
		this.detailedEstimate = detailedEstimate;
	}

	public EstimateAppropriation budgetUsage(String budgetUsage) {
		this.budgetUsage = budgetUsage;
		return this;
	}

	/**
	 * Refrernce of Budget Usage of the Estimate Appropriation
	 * 
	 * @return budgetUsage
	 **/
	@ApiModelProperty(required = true, value = "Refrernce of Budget Usage of the Estimate Appropriation")
	@NotNull

	@Size(min = 3, max = 100)
	public String getBudgetUsage() {
		return budgetUsage;
	}

	public void setBudgetUsage(String budgetUsage) {
		this.budgetUsage = budgetUsage;
	}

	public EstimateAppropriation auditDetails(AuditDetails auditDetails) {
		this.auditDetails = auditDetails;
		return this;
	}

	/**
	 * Get auditDetails
	 * 
	 * @return auditDetails
	 **/
	@ApiModelProperty(value = "")

	@Valid

	public AuditDetails getAuditDetails() {
		return auditDetails;
	}

	public void setAuditDetails(AuditDetails auditDetails) {
		this.auditDetails = auditDetails;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		EstimateAppropriation estimateAppropriation = (EstimateAppropriation) o;
		return Objects.equals(this.id, estimateAppropriation.id)
				&& Objects.equals(this.tenantId, estimateAppropriation.tenantId)
				&& Objects.equals(this.abstractEstimateDetails, estimateAppropriation.abstractEstimateDetails)
				&& Objects.equals(this.detailedEstimate, estimateAppropriation.detailedEstimate)
				&& Objects.equals(this.budgetUsage, estimateAppropriation.budgetUsage)
				&& Objects.equals(this.auditDetails, estimateAppropriation.auditDetails);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, tenantId, abstractEstimateDetails, detailedEstimate, budgetUsage, auditDetails);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class EstimateAppropriation {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
		sb.append("    abstractEstimateDetails: ").append(toIndentedString(abstractEstimateDetails)).append("\n");
		sb.append("    detailedEstimate: ").append(toIndentedString(detailedEstimate)).append("\n");
		sb.append("    budgetUsage: ").append(toIndentedString(budgetUsage)).append("\n");
		sb.append("    auditDetails: ").append(toIndentedString(auditDetails)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}
