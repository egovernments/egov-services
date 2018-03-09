package org.egov.user.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuditDetails {

	private String createdBy;
	private String lastModifiedBy;
	private Long createdTime;
	private Long lastModifiedTime;
}
