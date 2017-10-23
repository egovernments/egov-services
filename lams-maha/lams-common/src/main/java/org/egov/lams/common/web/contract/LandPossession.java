package org.egov.lams.common.web.contract;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LandPossession {
	@NotNull
	private Long id;
	private String possessionNumber;
	private String tenantId;
	private String landType;
	private UsageType usageType;
	private String subUsageType;
	private String ctsNumber;
	private String surveyNumber;
	private String remarks;
	@NotNull
	private Integer possessionDate;
	private String tdrCertificate;
	@NotNull
	private String landAcquisition;
	private String status;
	private WorkFlowDetails workFlowDetails;
	private AuditDetails auditDetails;

}
