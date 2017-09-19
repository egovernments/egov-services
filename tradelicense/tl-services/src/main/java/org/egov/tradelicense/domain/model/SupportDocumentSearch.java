package org.egov.tradelicense.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupportDocumentSearch {

	private Long id;

	private Long documentTypeId;
	
	private String tenantId;
	
	private Long applicationId;

	private String documentTypeName;

	private String fileStoreId;

	private String comments;

	private AuditDetails auditDetails;
}
