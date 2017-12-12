package org.egov.dataupload.model;

import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploaderRequest {
	
	@JsonProperty("RequestInfo")
	public RequestInfo requestInfo;
	
	@JsonProperty("moduleName")
	public String moduleName;
	
	@JsonProperty("masterName")
	public String masterName;
	
	@JsonProperty("tenantId")
	public String tenantId;
	
	@JsonProperty("fileStoreId")
	public String fileStoreId;
	

}
