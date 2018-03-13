package org.egov.user.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.egov.user.web.contract.AuditDetails;

@Getter
@Setter
@Builder
public class Otp {
	
	private Long userId;
	private String tenantId;
	private String identity;
	private String otp;
	private String validated;
	private AuditDetails auditDetails;

}
