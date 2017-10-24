package org.egov.works.estimate.web.model;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.works.commons.domain.model.AuditDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * An Object holds the basic data for a Detailed Estimate Deduction
 */
@ApiModel(description = "An Object holds the basic data for a Detailed Estimate Deduction")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-24T10:20:21.690Z")

public class DetailedEstimateDeduction {
	@JsonProperty("id")
	private String id = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("chartOfAccounts")
	private String chartOfAccounts = null;

	@JsonProperty("detailedEstimate")
	private DetailedEstimate detailedEstimate = null;

	@JsonProperty("percentage")
	private Integer percentage = null;

	@JsonProperty("amount")
	private Integer amount = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

	public DetailedEstimateDeduction id(String id) {
		this.id = id;
		return this;
	}

	/**
	 * Unique Identifier of the Detailed Estimate Deduction
	 * 
	 * @return id
	 **/
	@ApiModelProperty(value = "Unique Identifier of the Detailed Estimate Deduction")

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DetailedEstimateDeduction tenantId(String tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	/**
	 * Tenant id of the Detailed Estimate Deduction
	 * 
	 * @return tenantId
	 **/
	@ApiModelProperty(required = true, value = "Tenant id of the Detailed Estimate Deduction")
	@NotNull

	@Size(min = 4, max = 128)
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public DetailedEstimateDeduction chartOfAccounts(String chartOfAccounts) {
		this.chartOfAccounts = chartOfAccounts;
		return this;
	}

	/**
	 * Chart Of Accounts of the Detailed Estimate Deduction
	 * 
	 * @return chartOfAccounts
	 **/
	@ApiModelProperty(value = "Chart Of Accounts of the Detailed Estimate Deduction")

	public String getChartOfAccounts() {
		return chartOfAccounts;
	}

	public void setChartOfAccounts(String chartOfAccounts) {
		this.chartOfAccounts = chartOfAccounts;
	}

	public DetailedEstimateDeduction detailedEstimate(DetailedEstimate detailedEstimate) {
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

	public DetailedEstimateDeduction percentage(Integer percentage) {
		this.percentage = percentage;
		return this;
	}

	/**
	 * Percentage for the Detailed Estimate Deduction minimum: 0
	 * 
	 * @return percentage
	 **/
	@ApiModelProperty(value = "Percentage for the Detailed Estimate Deduction")

	@Min(0)
	public Integer getPercentage() {
		return percentage;
	}

	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}

	public DetailedEstimateDeduction amount(Integer amount) {
		this.amount = amount;
		return this;
	}

	/**
	 * Amount for the Detailed Estimate Deduction
	 * 
	 * @return amount
	 **/
	@ApiModelProperty(value = "Amount for the Detailed Estimate Deduction")

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public DetailedEstimateDeduction auditDetails(AuditDetails auditDetails) {
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
		DetailedEstimateDeduction detailedEstimateDeduction = (DetailedEstimateDeduction) o;
		return Objects.equals(this.id, detailedEstimateDeduction.id)
				&& Objects.equals(this.tenantId, detailedEstimateDeduction.tenantId)
				&& Objects.equals(this.chartOfAccounts, detailedEstimateDeduction.chartOfAccounts)
				&& Objects.equals(this.detailedEstimate, detailedEstimateDeduction.detailedEstimate)
				&& Objects.equals(this.percentage, detailedEstimateDeduction.percentage)
				&& Objects.equals(this.amount, detailedEstimateDeduction.amount)
				&& Objects.equals(this.auditDetails, detailedEstimateDeduction.auditDetails);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, tenantId, chartOfAccounts, detailedEstimate, percentage, amount, auditDetails);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class DetailedEstimateDeduction {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
		sb.append("    chartOfAccounts: ").append(toIndentedString(chartOfAccounts)).append("\n");
		sb.append("    detailedEstimate: ").append(toIndentedString(detailedEstimate)).append("\n");
		sb.append("    percentage: ").append(toIndentedString(percentage)).append("\n");
		sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
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
