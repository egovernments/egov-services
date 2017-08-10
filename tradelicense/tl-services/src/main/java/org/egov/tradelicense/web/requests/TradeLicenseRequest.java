package org.egov.tradelicense.web.requests;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tradelicense.web.contract.TradeLicenseContract;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeLicenseRequest {

	@JsonProperty("requestInfo")
	private RequestInfo requestInfo = new RequestInfo();

	@JsonProperty("licesnses")
	private List<TradeLicenseContract> licesnses = new ArrayList<TradeLicenseContract>();

}