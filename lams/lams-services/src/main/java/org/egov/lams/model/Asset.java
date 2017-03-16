package org.egov.lams.model;


import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
<<<<<<< d852f719e2afdbe653d81b070e62b33569ebf856
=======

>>>>>>> setting removed
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
	@JsonProperty("assetCategory")
	private AssetCategory category;
	
	@NotNull
	private String name;
	private Long doorNo;
	private String code;
	private Location locationDetails;
}
