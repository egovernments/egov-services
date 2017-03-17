package org.egov.commons.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UomCategory {
	
	private Long id ;
	private String tenantId;
	private String category;
	private String narration;

}
