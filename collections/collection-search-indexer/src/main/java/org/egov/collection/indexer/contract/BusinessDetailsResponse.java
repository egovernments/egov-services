package org.egov.collection.indexer.contract;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.egov.common.contract.response.ResponseInfo;

import java.util.List;

@Setter
@Getter
@ToString
public class BusinessDetailsResponse {
	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo;

	@JsonProperty("BusinessDetails")
	private List<BusinessDetailsRequestInfo> businessDetails;
}
