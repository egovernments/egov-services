package org.egov.contract;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
	  @NotNull
	  private RequestInfo requestInfo;

	  @Valid
	  @JsonProperty("Asset")
	  @NotNull
	  private Asset asset;

	  }
