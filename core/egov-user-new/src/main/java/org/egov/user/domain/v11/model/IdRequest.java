package org.egov.user.domain.v11.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class IdRequest {
	
	public String idName;
	
	public String tenantId;
	
	public String format;

}
