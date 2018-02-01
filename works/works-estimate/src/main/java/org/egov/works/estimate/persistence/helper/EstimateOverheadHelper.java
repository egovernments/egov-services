package org.egov.works.estimate.persistence.helper;

import java.math.BigDecimal;

import org.egov.works.estimate.web.contract.AuditDetails;
import org.egov.works.estimate.web.contract.DetailedEstimate;
import org.egov.works.estimate.web.contract.EstimateOverhead;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.egov.works.estimate.web.contract.Overhead;

/**
 * An Object holds the basic data of Estimate Overheads
 */
@ApiModel(description = "An Object holds the basic data of Estimate Overheads")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-30T09:25:28.667Z")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EstimateOverheadHelper {
	@JsonProperty("id")
	private String id = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("overhead")
	private String overhead = null;

	@JsonProperty("amount")
	private BigDecimal amount = null;

	@JsonProperty("detailedEstimate")
	private String detailedEstimate = null;

	@JsonProperty("createdBy")
	private String createdBy = null;

	@JsonProperty("lastModifiedBy")
	private String lastModifiedBy = null;

	@JsonProperty("createdTime")
	private Long createdTime = null;

	@JsonProperty("lastModifiedTime")
	private Long lastModifiedTime = null;
	
	public EstimateOverhead toDomain() {
		
		final EstimateOverhead estimateOverhead = new EstimateOverhead();
		estimateOverhead.setAuditDetails(new AuditDetails());
		estimateOverhead.getAuditDetails().setCreatedBy(this.createdBy);
		estimateOverhead.getAuditDetails().setCreatedTime(this.createdTime);
		estimateOverhead.getAuditDetails().setLastModifiedBy(this.lastModifiedBy);
		estimateOverhead.getAuditDetails().setLastModifiedTime(this.lastModifiedTime);
		estimateOverhead.setId(this.id);
		estimateOverhead.setTenantId(this.tenantId);
		estimateOverhead.setDetailedEstimate(this.detailedEstimate);
		estimateOverhead.setAmount(this.amount);
        Overhead overhead1 = new Overhead();
        overhead1.setCode(this.overhead);
        estimateOverhead.setOverhead(overhead1);
		return estimateOverhead;
	}
	
}
