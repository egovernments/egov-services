package org.egov.tradelicense.web.requests;

import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
import org.egov.tradelicense.web.contract.TradeLicenseContract;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeLicenseResponse {

	@JsonProperty("responseInfo")
	private ResponseInfo responseInfo;

	@JsonProperty("licenses")
	private List<TradeLicenseContract> licenses;
}