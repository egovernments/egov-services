package org.egov.asset.contract;

import java.util.List;

import org.egov.asset.contract.AssetCategory;
import org.egov.asset.contract.ResponseInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AssetCategoryResponse {


	 @JsonProperty("ResponseInfo")
	  private ResponseInfo resposneInfo = null;

	  @JsonProperty("AssetCategory")
	  private List<AssetCategory> assetCategory;
	
	
	
}
