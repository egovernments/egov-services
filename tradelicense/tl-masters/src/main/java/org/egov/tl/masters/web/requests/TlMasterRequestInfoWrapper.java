package org.egov.tl.masters.web.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TlMasterRequestInfoWrapper {

	@JsonProperty(value = "RequestInfo")
	private TlMasterRequestInfo requestInfo;
}
