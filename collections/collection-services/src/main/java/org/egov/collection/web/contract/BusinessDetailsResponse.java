package org.egov.collection.web.contract;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.egov.common.contract.response.ResponseInfo;

@Setter
@Getter
@ToString
public class BusinessDetailsResponse {
	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo;

	@JsonProperty("BusinessDetails")
	private List<BusinessDetailsRequestInfo> businessDetails;
}
