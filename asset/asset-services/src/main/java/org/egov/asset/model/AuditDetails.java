package org.egov.asset.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@EqualsAndHashCode
@ToString
public class AuditDetails {

	private String createdBy = null;

	private Long createdDate = null;

	private String lastModifiedBy = null;

	private Long lastModifiedDate = null;

}
