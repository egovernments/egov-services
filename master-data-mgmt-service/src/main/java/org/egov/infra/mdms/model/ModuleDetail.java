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
public class ModuleDetail {
	
	@NotNull
	private String moduleName;
	
	@NotNull
	@Valid
	private List<MasterDetail> masterDetails;

}
