package org.egov.lams.model;


import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Asset {

	@NotNull
	private Long id;
	
	@NotNull
	@JsonProperty("assetCategory")
	private AssetCategory category;
	
	private String name;
	private String code;
	private Location locationDetails;
}
