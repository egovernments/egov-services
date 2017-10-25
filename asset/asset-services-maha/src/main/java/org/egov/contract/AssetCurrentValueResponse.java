package org.egov.contract;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.model.AssetCurrentValue;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetCurrentValueResponse {
	
	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo;
	
	
	@JsonProperty("AssetCurrentValues")
	private List<AssetCurrentValue> assetCurrentValues;
	
}
