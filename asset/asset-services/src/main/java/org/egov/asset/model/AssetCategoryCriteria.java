package org.egov.asset.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
@Builder
public class AssetCategoryCriteria {
	
	  @JsonProperty("id")
	  private Long id = null;
	  
	  @JsonProperty("tenantId")
	  @NotNull
	  private String tenantId = null;

	  @JsonProperty("name")
	  private String name = null;

	  @JsonProperty("code")
	  private String code = null;

	  @JsonProperty("assetCategoryType")
	  private List<String> assetCategoryType = new ArrayList<>();

}
