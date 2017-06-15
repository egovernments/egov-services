package org.egov.commons.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
@Builder
@Getter
@AllArgsConstructor
public class BusinessCategory {
	


	private Long id;
	
	private String code;
	
	private String name;
	
	private Boolean active;
	
	private String tenantId;

	

}
