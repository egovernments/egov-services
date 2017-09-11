package org.egov.tl.commons.web.requests;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.TradeLicenseIndexerContract;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeLicenseIndexerRequest {

	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo = new RequestInfo();

	@JsonProperty("licenses")
	@Valid
	private List<TradeLicenseIndexerContract> licenses = new ArrayList<TradeLicenseIndexerContract>();
	
}