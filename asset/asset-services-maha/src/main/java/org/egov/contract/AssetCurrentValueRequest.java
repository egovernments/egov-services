package org.egov.contract;


import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.common.contract.request.RequestInfo;
import org.egov.model.CurrentValue;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class AssetCurrentValueRequest   {
	  @JsonProperty("RequestInfo")
	  private RequestInfo requestInfo = null;
      
	  @Valid
	  @NotNull
	  @JsonProperty("AssetCurrentValue")
	  private List<CurrentValue> assetCurrentValue = null;
}