package org.egov.citizen.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ServiceAttrDefination {
	
	private String serviceCode;
	private List<Attributes> attributes;

}
