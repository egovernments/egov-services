package org.egov.lams.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AssetResponse {

	  @JsonProperty("ResposneInfo")
	  private ResponseInfo resposneInfo = null;

	  @JsonProperty("Assets")
	  private List<Asset> assets;

	public ResponseInfo getResposneInfo() {
		return resposneInfo;
	}

	public void setResposneInfo(ResponseInfo resposneInfo) {
		this.resposneInfo = resposneInfo;
	}

	public List<Asset> getAssets() {
		return assets;
	}

	public void setAssets(List<Asset> assets) {
		this.assets = assets;
	}

	@Override
	public String toString() {
		return "AssetResponse [resposneInfo=" + resposneInfo + ", assets=" + assets + "]";
	}
	  
	  
	  
	  
}
