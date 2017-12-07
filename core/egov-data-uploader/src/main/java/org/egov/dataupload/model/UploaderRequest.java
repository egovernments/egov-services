package org.egov.dataupload.model;

import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UploaderRequest {
	
	@JsonProperty("RequestInfo")
	public RequestInfo requestInfo;
	
	@JsonProperty("moduleName")
	public String moduleName;
	
	@JsonProperty("maasterName")
	public String maasterName;
	
	@JsonProperty("fileStoreId")
	public String fileStoreId;
	

}
