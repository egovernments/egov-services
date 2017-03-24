package org.egov.lams.model;


import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Asset {

	private Long id;
	
	@NotNull
	@JsonProperty("assetCategory")
	private AssetCategory category;
	
	@NotNull
	private String name;
	private Long doorNo;
	private String code;
	private Location locationDetails;
}