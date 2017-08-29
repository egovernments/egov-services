package org.egov.asset.contract;

import java.util.List;

import javax.validation.Valid;

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
	@Valid
	private RequestInfo requestInfo;
	
	@JsonProperty("AssetCurrentValues")
	@Valid
	private List<AssetCurrentValue> assetCurrentValues;
}
