package org.egov.boundary.domain.model;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrossHierarchy {

	private Long id;
	private String code;
	@NotNull
	private String tenantId;

}
