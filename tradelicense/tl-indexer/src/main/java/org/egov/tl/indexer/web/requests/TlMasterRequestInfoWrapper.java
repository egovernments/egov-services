package org.egov.tl.indexer.web.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TlMasterRequestInfoWrapper {

	@JsonProperty(value = "RequestInfo")
	private TlMasterRequestInfo requestInfo;
}
