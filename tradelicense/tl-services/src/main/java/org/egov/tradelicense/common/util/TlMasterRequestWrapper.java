package org.egov.tradelicense.common.util;

import org.egov.tl.commons.web.contract.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TlMasterRequestWrapper {

	@JsonProperty(value = "RequestInfo")
	private RequestInfo requestInfo;
}