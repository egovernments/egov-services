package org.egov.lams.common.web.contract;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LandAcquisition {

	private Long id;
	@NotNull
	private String tenantId;
	private String landAcquisitionNumber;
	private String landType;
	private UsageType usageType;
	private String subUsageType;
	private String ctsNumber;
	private String surveyNumber;
	private String plotNumber;
	private String plotArea;
	private String plotAddress;
	private String location;
	private String pinCode;
	private String reservationCode;
	private String resolutionNumber;
	private Long resolutionDate;
	private Long latitude;
	private Long longitude;
	private String landAcquisitionOfficerName;
	private String landAcquisitionOfficerRemark;
	@NotNull
	private String paperNoticeNumber;
	@NotNull
	private String advocateName;
	private Long panelAppoIntegermentDate;
	private String status;
	private WorkFlowDetails workFlowDetails;
	private String stateId;
	private AuditDetails auditDetails;
	private List<LandPossession> possessionOfLand;
	private List<LandTransfer> landTransfer;
	private ValuationDetails valuationDetails;
	private ProposalDetails proposalDetails;
	private List<SupportDocuments> supportDocuments;

}
