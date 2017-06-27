package org.egov.commons.web.contract;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Setter;
@Setter
public class BusinessDetailsResponse {
	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo;
	
	@JsonProperty("BusinessDetailsInfo")
	private List<BusinessDetailsRequestInfo> businessDetails;
}
