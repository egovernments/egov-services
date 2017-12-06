package org.egov.inv.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class Tenant {

	private Long id;
	private String code;
	private String name;
	private String description;
	private String logoId;
	private String imageId;
	private String domainUrl;
	private String type;
	private String twitterUrl;
	private String facebookUrl;
	private String emailId;
	private String address;
	private String contactNumber;
	private String helpLineNumber;
	private City city;
 
	
}