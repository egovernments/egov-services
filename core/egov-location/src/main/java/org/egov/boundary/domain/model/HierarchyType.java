package org.egov.boundary.domain.model;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HierarchyType {

	private Long id;
	private String name;
	private String code;
	private String localName;
	@NotNull
	private String tenantId;

}
