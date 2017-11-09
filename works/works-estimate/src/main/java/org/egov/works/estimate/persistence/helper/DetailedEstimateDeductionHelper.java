package org.egov.works.estimate.persistence.helper;

import java.math.BigDecimal;

import org.egov.works.estimate.web.contract.AuditDetails;
import org.egov.works.estimate.web.contract.ChartOfAccount;
import org.egov.works.estimate.web.contract.DetailedEstimate;
import org.egov.works.estimate.web.contract.DetailedEstimateDeduction;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * An Object that holds the basic data of Detailed Estimate Deductions
 */
@ApiModel(description = "An Object that holds the basic data of Detailed Estimate Deductions")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-30T10:26:20.111Z")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DetailedEstimateDeductionHelper {
	@JsonProperty("id")
	private String id = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("chartOfAccounts")
	private String chartOfAccounts = null;

	@JsonProperty("detailedEstimate")
	private String detailedEstimate = null;

	@JsonProperty("percentage")
	private Double percentage = null;

	@JsonProperty("amount")
	private BigDecimal amount = null;
	
	@JsonProperty("createdBy")
	private String createdBy = null;

	@JsonProperty("lastModifiedBy")
	private String lastModifiedBy = null;

	@JsonProperty("createdTime")
	private Long createdTime = null;

	@JsonProperty("lastModifiedTime")
	private Long lastModifiedTime = null;

	public DetailedEstimateDeduction toDomain() {
		
		final DetailedEstimateDeduction detailedEstimateDeduction = new DetailedEstimateDeduction();
		detailedEstimateDeduction.setAuditDetails(new AuditDetails());
		detailedEstimateDeduction.getAuditDetails().setCreatedBy(this.createdBy);
		detailedEstimateDeduction.getAuditDetails().setCreatedTime(this.createdTime);
		detailedEstimateDeduction.getAuditDetails().setLastModifiedBy(this.lastModifiedBy);
		detailedEstimateDeduction.getAuditDetails().setLastModifiedTime(this.lastModifiedTime);
		detailedEstimateDeduction.setId(this.id);
		detailedEstimateDeduction.setTenantId(this.tenantId);
		detailedEstimateDeduction.setDetailedEstimate(this.detailedEstimate);
		detailedEstimateDeduction.setAmount(this.amount);
		detailedEstimateDeduction.setPercentage(this.percentage);
		ChartOfAccount chartOfAccount = new ChartOfAccount();
		chartOfAccount.setGlcode(this.chartOfAccounts);
		detailedEstimateDeduction.setChartOfAccounts(chartOfAccount);
		return detailedEstimateDeduction;
	}

}
