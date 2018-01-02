package org.egov.filter.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Mdms {
	
	private String moduleName;
	private List<MasterDetail> masterDetails;
}
