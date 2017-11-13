package org.egov.works.estimate.persistence.helper;

import org.egov.works.estimate.web.contract.AuditDetails;
import org.egov.works.estimate.web.contract.DetailedEstimate;
import org.egov.works.estimate.web.contract.FinancialYear;
import org.egov.works.estimate.web.contract.MultiYearEstimate;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * An Object that holds the basic data of Multi Year Estimate
 */
@ApiModel(description = "An Object that holds the basic data of Multi Year Estimate")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-30T10:26:20.111Z")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MultiYearEstimateHelper   {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("financialYear")
    private String financialYear = null;

    @JsonProperty("detailedEstimate")
    private String detailedEstimate = null;

    @JsonProperty("percentage")
    private Double percentage = null;
    
	@JsonProperty("createdBy")
	private String createdBy = null;

	@JsonProperty("lastModifiedBy")
	private String lastModifiedBy = null;

	@JsonProperty("createdTime")
	private Long createdTime = null;

	@JsonProperty("lastModifiedTime")
	private Long lastModifiedTime = null;

	public MultiYearEstimate toDomain() {
		MultiYearEstimate multiYearEstimate = new MultiYearEstimate();
		multiYearEstimate.setAuditDetails(new AuditDetails());
		multiYearEstimate.getAuditDetails().setCreatedBy(this.createdBy);
		multiYearEstimate.getAuditDetails().setCreatedTime(this.createdTime);
		multiYearEstimate.getAuditDetails().setLastModifiedBy(this.lastModifiedBy);
		multiYearEstimate.getAuditDetails().setLastModifiedTime(this.lastModifiedTime);
		multiYearEstimate.setId(this.id);
		multiYearEstimate.setTenantId(this.tenantId);
		multiYearEstimate.setDetailedEstimate(this.detailedEstimate);
		FinancialYear financialYear = new FinancialYear();
		financialYear.setId(this.financialYear);
		multiYearEstimate.setFinancialYear(financialYear);
		multiYearEstimate.setPercentage(this.percentage);
		
		return multiYearEstimate;
	}

}
