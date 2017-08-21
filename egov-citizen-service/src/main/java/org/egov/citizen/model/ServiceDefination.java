package org.egov.citizen.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ServiceDefination {
	
	@JsonProperty(value = "service_code")
	private String serviceCode;
	@JsonProperty(value = "service_name")
	private String serviceName;
	private String description;
	private Boolean metadata;
	private String type;
	private String keywords;
	private String group;
	private List<Attributes> attributes;

}
