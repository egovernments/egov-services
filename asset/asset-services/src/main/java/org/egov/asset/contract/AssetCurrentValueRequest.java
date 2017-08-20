package org.egov.asset.contract;

import java.util.List;

import org.egov.asset.model.AssetCurrentValue;
import org.egov.common.contract.request.RequestInfo;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetCurrentValueRequest {

	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;
	
	@JsonProperty("AssetCurrentValues")
	private List<AssetCurrentValue> assetCurrentValues;
}
