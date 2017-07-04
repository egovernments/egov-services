package org.egov.common.domain.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Auditable {
	protected String tenantId;
	protected User createdBy;
	protected User lastModifiedBy;
	protected Date createdDate;
	protected Date lastModifiedDate;

}
