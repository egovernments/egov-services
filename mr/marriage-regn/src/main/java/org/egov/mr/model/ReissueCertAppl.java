package org.egov.mr.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.egov.mr.model.enums.ApplicationStatus;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Builder
public class ReissueCertAppl {
	
	private String id;

	private String regnNo;

	private ReissueApplicantInfo applicantInfo;

	private List<MarriageDocument> documents = new ArrayList<MarriageDocument>();

	private String applicationNumber;
	
	@NotNull
	private ApplicationStatus reissueApplStatus;
	
	private String stateId;
	
	private ApprovalDetails approvalDetails;

	private List<Long> demands = new ArrayList<Long>();

	@NotNull
	private String tenantId;

	private MarriageCertificate certificate;
	
	private String rejectionReason;
	
	private String remarks;
	
	private Boolean isActive;
	
	private AuditDetails auditDetails;
}
