package org.egov.wcms.transaction.model;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConnectionDocument {
	
	@NotNull
	private Long id;

	@NotNull
	private String documentType;

	@NotNull
	private String referenceNumber;

	@NotNull
	private String fileStoreId;
	
	@NotNull
	private Long connectionId;
	
	@NotNull
	private String tenantId;
	
	@NotNull
	private Long createdBy;
	
	private Long createdDate;
	
	@NotNull
	private Long lastModifiedBy;
	
	private Long lastModifiedDate;

}
