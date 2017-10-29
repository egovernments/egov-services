package org.egov.contract;

import org.egov.common.contract.request.RequestInfo;
import org.egov.model.Asset;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetRequest   {
	  @JsonProperty("RequestInfo")
	  private RequestInfo requestInfo;

	  @JsonProperty("Asset")
	  private Asset asset;

	  }
