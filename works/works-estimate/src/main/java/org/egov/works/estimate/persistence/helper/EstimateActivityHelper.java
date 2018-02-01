package org.egov.works.estimate.persistence.helper;

import java.math.BigDecimal;

import org.egov.works.estimate.web.contract.AuditDetails;
import org.egov.works.estimate.web.contract.DetailedEstimate;
import org.egov.works.estimate.web.contract.EstimateActivity;
import org.egov.works.estimate.web.contract.NonSOR;
import org.egov.works.estimate.web.contract.RevisionType;
import org.egov.works.estimate.web.contract.ScheduleOfRate;
import org.egov.works.estimate.web.contract.UOM;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * An Object holds the basic data of Estimate Activity
 */
@ApiModel(description = "An Object holds the basic data of Estimate Activity")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-10-30T10:26:20.111Z")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EstimateActivityHelper {
	@JsonProperty("id")
	private String id = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("scheduleOfRate")
	private String scheduleOfRate = null;

	@JsonProperty("nonSor")
	private String nonSor = null;

	@JsonProperty("uom")
	private String uom = null;

	@JsonProperty("unitRate")
	private BigDecimal unitRate = null;

	@JsonProperty("estimateRate")
	private BigDecimal estimateRate = null;

	@JsonProperty("quantity")
	private Double quantity = null;

	@JsonProperty("serviceTaxPerc")
	private Double serviceTaxPerc = null;

	@JsonProperty("revisionType")
	private String revisionType = null;

	@JsonProperty("parent")
	private String parent = null;

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

	public EstimateActivity toDomain() {

		final EstimateActivity estimateActivity = new EstimateActivity();
		estimateActivity.setAuditDetails(new AuditDetails());
		estimateActivity.getAuditDetails().setCreatedBy(this.createdBy);
		estimateActivity.getAuditDetails().setCreatedTime(this.createdTime);
		estimateActivity.getAuditDetails().setLastModifiedBy(this.lastModifiedBy);
		estimateActivity.getAuditDetails().setLastModifiedTime(this.lastModifiedTime);
		estimateActivity.setId(this.id);
		estimateActivity.setTenantId(this.tenantId);
		estimateActivity.setDetailedEstimate(this.detailedEstimate);
		estimateActivity.setEstimateRate(this.estimateRate);
		NonSOR nonSOR = null;
		if(this.nonSor != null) {
            nonSOR = new NonSOR();
            nonSOR.setId(this.nonSor);
        }
		estimateActivity.setNonSor(nonSOR);
		ScheduleOfRate scheduleOfRate = new ScheduleOfRate();
		scheduleOfRate.setId(this.scheduleOfRate);
        estimateActivity.setScheduleOfRate(scheduleOfRate);
		UOM uom = new UOM();
		uom.setCode(this.uom);
		estimateActivity.setUom(uom);
		estimateActivity.setQuantity(this.quantity);
		if(this.revisionType != null)
			estimateActivity.setRevisionType(RevisionType.valueOf(this.revisionType));
		estimateActivity.setServiceTaxPerc(this.serviceTaxPerc);
		estimateActivity.setUnitRate(this.unitRate);
		return estimateActivity;
	}

}
