package org.egov.infra.mdms.model;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class MdmsCriteria {
	
	@NotNull
	private String tenantId;
	
	@NotNull
	@Valid
	private List<ModuleDetail> moduleDetails;

}
