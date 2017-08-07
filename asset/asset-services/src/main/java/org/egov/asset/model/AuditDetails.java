package org.egov.asset.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AuditDetails {

	private String createdBy = null;

	private Long createdDate = null;

	private String lastModifiedBy = null;

	private Long lastModifiedDate = null;

}
