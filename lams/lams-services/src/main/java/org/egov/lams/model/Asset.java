package org.egov.lams.model;


import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Asset {

	private Long id;
	
	@NotNull
	private Long category;
	
	@NotNull
	private String name;
	private Long doorNo;
	private String code;
	private Location locationDetails;
}
