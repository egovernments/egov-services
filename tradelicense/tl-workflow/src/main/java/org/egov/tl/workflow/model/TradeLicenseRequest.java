package org.egov.tl.workflow.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeLicenseRequest {

	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo = new RequestInfo();

	@JsonProperty("licenses")
	@Valid
	private List<TradeLicenseContract> licenses = new ArrayList<TradeLicenseContract>();

}