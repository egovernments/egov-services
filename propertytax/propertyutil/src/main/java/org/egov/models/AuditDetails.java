package org.egov.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditDetails {

	private String createdBy;

	private String createdDate;

	private String lastModifiedBy;

	private String lastModifiedDate;
}
