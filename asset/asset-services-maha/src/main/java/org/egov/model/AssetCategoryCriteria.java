package org.egov.model;

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
	  private Long id;
	  
	  @JsonProperty("tenantId")
	  @NotNull
	  private String tenantId;

	  @JsonProperty("name")
	  private String name;

	  @JsonProperty("code")
	  private String code;

	  @JsonProperty("assetCategoryType")
	  private List<String> assetCategoryType = new ArrayList<String>();

}