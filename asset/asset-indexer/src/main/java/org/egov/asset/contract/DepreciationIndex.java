package org.egov.asset.contract;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.DepreciationDetail;
import org.egov.asset.model.enums.DepreciationStatus;
import org.egov.asset.model.enums.ReasonForFailure;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepreciationIndex {

	private Long voucherReference;

	private AuditDetails auditDetails;
	
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("assetId")
	private Long assetId = null;
	
	@JsonProperty("status")
	private DepreciationStatus status = null;

	@JsonProperty("depreciationRate")
	private Double depreciationRate = null;

	@JsonProperty("valueBeforeDepreciation")
	private BigDecimal valueBeforeDepreciation = null;

	@JsonProperty("depreciationValue")
	private BigDecimal depreciationValue = null;

	@JsonProperty("valueAfterDepreciation")
	private BigDecimal valueAfterDepreciation = null;
	
	private ReasonForFailure reasonForFailure;
	
	@JsonProperty("tenantId")
	private String tenantId;

	@JsonProperty("financialYear")
	private String financialYear;

	@JsonProperty("fromDate")
	private Long fromDate;

	@JsonProperty("toDate")
	private Long toDate;

	public static DepreciationIndex toDepreciationIndex(DepreciationDetail depreciationDetail,Long voucherReference,
			AuditDetails auditDetails, String tenantId,String financialYear, Long fromDate,Long toDate ){
		
		return DepreciationIndex.builder().auditDetails(auditDetails).voucherReference(voucherReference)
				.id(depreciationDetail.getId()).assetId(depreciationDetail.getAssetId()).tenantId(tenantId)
				.depreciationRate(depreciationDetail.getDepreciationRate()).financialYear(financialYear)
				.depreciationValue(depreciationDetail.getDepreciationValue()).fromDate(fromDate)
				.reasonForFailure(depreciationDetail.getReasonForFailure()).status(depreciationDetail.getStatus())
				.valueAfterDepreciation(depreciationDetail.getValueAfterDepreciation()).toDate(toDate)
				.valueBeforeDepreciation(depreciationDetail.getValueBeforeDepreciation())
				.build();
	}
}
