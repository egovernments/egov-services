package org.egov.infra.mdms.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class MdmsCriteria {
	
	private String tenantId;
	private List<ModuleDetail> moduleDetails;

}
