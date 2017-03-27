package org.egov.lams.web.contract;

import java.util.List;

import org.egov.lams.model.Asset;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AssetResponse {

	@JsonProperty("ResposneInfo")
	private ResponseInfo responseInfo;
	
	@JsonProperty("Assets")
	private List<Asset> assets;
}
