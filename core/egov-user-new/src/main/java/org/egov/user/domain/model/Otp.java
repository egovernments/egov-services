package org.egov.user.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
