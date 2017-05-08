package org.egov.asset.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
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
public class AssetCategoryCriteria {
	
	  @JsonProperty("id")
	  private Long id = null;
	  
	  @JsonProperty("tenantId")
	  private String tenantId = null;

	  @JsonProperty("name")
	  private String name = null;

	  @JsonProperty("code")
	  private String code = null;

	  @JsonProperty("assetCategoryType")
	  private List<String> assetCategoryType = new ArrayList<String>();

}
