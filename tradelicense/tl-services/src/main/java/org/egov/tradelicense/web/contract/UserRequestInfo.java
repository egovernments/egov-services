package org.egov.tradelicense.web.contract;

import org.egov.tl.commons.web.contract.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestInfo {
	@JsonProperty("User")
	private User user;
	@JsonProperty("RequestInfo")
	private RequestInfo RequestInfo;
}
