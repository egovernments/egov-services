package org.egov.lams.notification.model;


import javax.validation.constraints.NotNull;

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

	@NotNull
	private Long id;
	
	@NotNull
	@JsonProperty("assetCategory")
	private AssetCategory category;
	
	private String name;
	private String code;
	private Location locationDetails;
}
