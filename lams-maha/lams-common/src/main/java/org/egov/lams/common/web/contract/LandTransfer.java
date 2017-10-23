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
public class LandTransfer {
	@NotNull
	private Long id;
	private String tenantId;
	private String landTransferNumber;
	private String landAcquisition;
	@NotNull
	private String resolutionNumber;
	private Integer resolutionDate;
	private String propertyId;
	private String department;
	private String transferType;
	private String transferPurpose;
	private String transferFrom;
	private String transferTo;
	private String transferNumber;
	private Integer transferedFromDate;
	private Integer transferedToDate;
	private String remark;
	private String status;
	private WorkFlowDetails workFlowDetails;
	private AuditDetails auditDetails;
}