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
public class SupportDocuments {
	private Long id;
	@NotNull
	private String tenantId;
	@NotNull
	private Integer landAcquisitonId;
	@NotNull
	private String fileStoreId;
	private String comments;
	private AuditDetails auditDetails;

}
