package org.egov.asset.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class RevaluationIndex {

	private String tenantId;
	private Long revaluationId;

	private Double currentCapitalizedValue;
	private String typeOfChange;
	private Double revaluationAmount;
	private Double valueAfterRevaluation;
	private Long revaluationDate;
	private String reevaluatedBy;
	private String reasonForRevaluation;
	private Long fixedAssetsWrittenOffAccount;

	private Long assetId;
	private String assetCode;
	private String assetName;

	private Long functionId;
	private String functionName;
	private String functionCode;

	private Long fundId;
	private String fundName;
	private String fundCode;

	private Long schemeId;
	private String schemeName;
	private String schemeCode;

	private Long subSchemeId;
	private String subSchemeCode;
	private String subSchemeName;

	private String comments;
	private String status;
	
	private String createdBy;
	private Long createdDate;
	private String lastModifiedBy;
	private Long lastModifiedDate;

	public void setRevaluationData(final Revaluation revaluation) {
		tenantId = revaluation.getTenantId();
		revaluationId = revaluation.getId();
		currentCapitalizedValue = revaluation.getCurrentCapitalizedValue();
		typeOfChange = revaluation.getTypeOfChange().toString();
		revaluationAmount = revaluation.getRevaluationAmount();
		valueAfterRevaluation = revaluation.getValueAfterRevaluation();
		revaluationDate = revaluation.getRevaluationDate();
		reevaluatedBy = revaluation.getReevaluatedBy();
		reasonForRevaluation = revaluation.getReasonForRevaluation();
		fixedAssetsWrittenOffAccount = revaluation.getFixedAssetsWrittenOffAccount();
		comments = revaluation.getComments();
		status = revaluation.getStatus().toString();
	}

}
