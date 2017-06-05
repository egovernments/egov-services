package org.egov.wcms.model;

import javax.validation.constraints.NotNull;


import lombok.*;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
@Builder
public class Property {

	@NotNull
	private long id;
	
	@NotNull
	private String usageType;
	
	@NotNull
	private String propertyType;
	
	@NotNull
	private String address;
}

//This object needs modification based on the response received from property tax module.
