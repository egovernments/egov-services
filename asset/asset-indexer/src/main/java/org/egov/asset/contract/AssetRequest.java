package org.egov.asset.contract;


import org.egov.asset.contract.RequestInfo;
import org.egov.asset.model.Asset;

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
public class AssetRequest {

	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;
	@JsonProperty("Asset")
	private Asset asset;

	
}
