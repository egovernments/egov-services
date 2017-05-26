package org.egov.mr.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

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

	@NotNull
	private String regnNo;

	private ReissueApplicantInfo applicantInfo;

	private List<MarriageDocument> documents = new ArrayList<MarriageDocument>();

	private String applicationNumber;

	private String stateId;

	private ApprovalDetails approvalDetails;

	private List<Long> demands = new ArrayList<Long>();

	@NotNull
	private String tenantId;

	private List<MarriageCertificate> certificates = new ArrayList<MarriageCertificate>();
}
