package org.egov.lams.model;

import com.fasterxml.jackson.annotation.JsonProperty;

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
	private Long category;
	private String name;
	private Long doorNo;
	private String code;
	
	@JsonProperty("locationDetails")
	private Location locality;
}
