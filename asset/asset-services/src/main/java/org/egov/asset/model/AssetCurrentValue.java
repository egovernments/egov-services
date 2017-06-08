package org.egov.asset.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AssetCurrentValue {
	
	@JsonProperty("tenantId")
	@NotNull
	private String tenantId;
	
	@JsonProperty("assetId")
	@NotNull
	private Long assetId;
	
	@JsonProperty("currentAmmount")
	private Double currentAmmount;


}
