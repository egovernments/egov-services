package org.egov.lams.common.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Designation {
	
	private Long id;
	private String name;
	private String code;
	private String description;
	private String chartOfAccounts;
	private Boolean active;
	private String tenantId;
}
