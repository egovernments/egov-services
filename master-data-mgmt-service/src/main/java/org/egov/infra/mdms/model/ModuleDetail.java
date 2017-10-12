package org.egov.infra.mdms.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ModuleDetail {
	
	private String moduleName;
	private List<MasterDetail> masterDetails;

}
