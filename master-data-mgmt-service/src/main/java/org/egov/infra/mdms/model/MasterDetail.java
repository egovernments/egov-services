package org.egov.infra.mdms.model;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class MasterDetail {
	
	@NotNull
	private String name;
	
	private String filter;
}
